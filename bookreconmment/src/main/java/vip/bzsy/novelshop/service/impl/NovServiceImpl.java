package vip.bzsy.novelshop.service.impl;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vip.bzsy.novelshop.common.CommonUtils;
import vip.bzsy.novelshop.mapper.*;
import vip.bzsy.novelshop.model.*;
import vip.bzsy.novelshop.model.vo.*;
import vip.bzsy.novelshop.service.NovService;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class NovServiceImpl implements NovService {

    @Override
    public int publishNovel(PublishNovelReq req, Claims claims) {
        BsNov bsNov = new BsNov();
        Integer userId = (Integer) claims.get("userId");
        bsNov.setEditorid(userId);
        String name = (String) claims.get("name");
        bsNov.setEditorname(name);
        bsNov.setSort(req.getSort());
        bsNov.setCover(req.getCover());
        bsNov.setPrice(req.getPrice());
        bsNov.setIntroduction(req.getIntroduction());
        bsNov.setNovelname(req.getNovelName());
        bsNov.setTime(req.getTime());
        return novMapper.insertSelective(bsNov);
    }

    @Override
    public List<GetPublishedNovelRes> getPublishedNovel(Integer userId) {
        BsNovExample example = new BsNovExample();
        BsNovExample.Criteria criteria = example.createCriteria();
        criteria.andEditoridEqualTo(userId);
        List<BsNov> list = novMapper.selectByExample(example);
        List<GetPublishedNovelRes> listRep = new ArrayList<>();
        for (BsNov bsNov:list){
            GetPublishedNovelRes res = new GetPublishedNovelRes();
            res.setCover(bsNov.getCover());
            res.setId(bsNov.getId());
            res.setIntroduction(bsNov.getIntroduction());
            res.setNovelname(bsNov.getNovelname());
            listRep.add(res);
        }
        return listRep;
    }

    @Override
    public int deleteNovel(Integer id) {
        return novMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer uploadChapter(UploadChapterReq req) throws Exception{
        MultipartFile file = req.getFile();
        InputStream inputStream = file.getInputStream();
        Map<String, String> map = CommonUtils.resolveCode(inputStream);
        InputStreamReader reader = new InputStreamReader(inputStream,map.get("code"));
        BufferedReader bufferedReader = new BufferedReader(reader);
        String content = map.get("text");
        //去空格并且+<p>标签
        content = "<p>"+content.trim();
        String lineTxt = "";
        while ((lineTxt = bufferedReader.readLine()) != null){
            content += lineTxt;
        }
        //content = content.replaceAll("<p>","").replaceAll("</p>","");
        //结束的时候拼接上</p>
        content += "</p>";
        bufferedReader.close();
        BsChapter chapter = new BsChapter();
        chapter.setNovelid(req.getId());
        chapter.setContent(content);
        chapter.setStatus(1);
        chapterMapper.insertSelective(chapter);
        return chapter.getId();
    }

    @Override
    public List<GetNovelChapterRes> getNovelChapter(Integer id) {
        BsChapterExample example = new BsChapterExample();
        example.setOrderByClause("chapter asc");
        BsChapterExample.Criteria criteria = example.createCriteria();
        criteria.andNovelidEqualTo(id);
        criteria.andStatusEqualTo(0);
        List<BsChapter> list = chapterMapper.selectByExample(example);
        List<GetNovelChapterRes> list1 = new ArrayList<>();
        for (BsChapter bsChapter : list){
            GetNovelChapterRes res = new GetNovelChapterRes();
            res.setId(bsChapter.getId());
            res.setChapter(bsChapter.getChapter());
            res.setTitle(bsChapter.getTitle());
            list1.add(res);
        }
        return list1;
    }

    @Override
    public Integer addChapter(AddChapterReq req) {
        BsChapter chapter = new BsChapter();
        chapter.setId(req.getId());
        chapter.setStatus(0);
        chapter.setContent(req.getContent());
        chapter.setNovelid(req.getNovelId());
        chapter.setChapter(req.getChapter());
        chapter.setTitle(req.getTitle());
        if (req.getUpload() == 0){
            return chapterMapper.insertSelective(chapter);
        } else if (req.getUpload() == 1){
            return chapterMapper.updateByPrimaryKeySelective(chapter);
        }
        return null;
    }

    @Override
    public List<FetDraftBoxRes> getDraftBox(Claims claims) {
        Integer userId = (Integer)claims.get("userId");
        BsNovExample example = new BsNovExample();
        BsNovExample.Criteria criteria = example.createCriteria();
        criteria.andEditoridEqualTo(userId);
        List<BsNov> bsNovList = novMapper.selectByExample(example);
        List<FetDraftBoxRes> fetDraftBoxResList = new ArrayList<>();
        for (BsNov bsNov : bsNovList){
            Integer novId = bsNov.getId();
            BsChapterExample example1 = new BsChapterExample();
            example1.setOrderByClause("chapter asc");
            BsChapterExample.Criteria criteria1 = example1.createCriteria();
            criteria1.andNovelidEqualTo(novId);
            criteria1.andStatusEqualTo(3);
            List<BsChapter> bsChapters = chapterMapper.selectByExample(example1);
            for (BsChapter bsChapter : bsChapters) {
                FetDraftBoxRes res = new FetDraftBoxRes();
                res.setNovelName(bsNov.getNovelname());
                res.setId(bsChapter.getId());
                res.setChapter(bsChapter.getChapter());
                res.setTitle(bsChapter.getTitle());
                fetDraftBoxResList.add(res);
            }
        }
        return fetDraftBoxResList;
    }

    @Override
    public Integer addChapterToDraft(AddChapterToDraftReq req) {
        BsChapter chapter = new BsChapter();
        chapter.setTitle(req.getTitle());
        chapter.setContent(req.getContent());
        chapter.setNovelid(req.getNovelId());
        chapter.setChapter(req.getChapter());
        chapter.setStatus(3);
        return chapterMapper.insertSelective(chapter);
    }

    @Override
    public GetDraftRes getDraft(Integer id) {
        BsChapter chapter = chapterMapper.selectByPrimaryKey(id);
        GetDraftRes res = new GetDraftRes();
        res.setChapter(chapter.getChapter());
        res.setContent(chapter.getContent());
        res.setNovelId(chapter.getNovelid());
        res.setTitle(chapter.getTitle());
        return res;
    }

    @Override
    public Integer saveChapterToDraft(BsChapter chapter) {
        return chapterMapper.updateByPrimaryKeySelective(chapter);
    }

    @Override
    public Integer deleteDraft(Integer id) {
        return chapterMapper.deleteByPrimaryKey(id);
    }

   /* @Override
    public List<GetNewPublishRes> getNewPublish() {
        BsNovExample example = new BsNovExample();
        example.setOrderByClause("time desc");
        List<BsNov> bsNovList = novMapper.selectByExample(example);
        List<GetNewPublishRes> list = new ArrayList<>();
        for (BsNov bsNov:bsNovList){
            GetNewPublishRes res = new GetNewPublishRes();
            res.setId(bsNov.getId());
            res.setCover(bsNov.getCover());
            res.setIntroduction(bsNov.getIntroduction());
            res.setNovelName(bsNov.getNovelname());
            list.add(res);
        }
        return list;
    }*/


    @Override
    public List<GetNewPublishRes> getNewPublish() {
        List<GetNewPublishRes> list = novMapper.getNewPublish();
        return list;
    }

    @Override
    public List<GetNewPublishRes> getRanking(String sort) {
        BsNovExample example = new BsNovExample();
        example.setOrderByClause(sort + " desc");
        List<BsNov> bsNovList = novMapper.selectByExample(example);
        List<GetNewPublishRes> list = new ArrayList<>();
        for (BsNov bsNov:bsNovList){
            GetNewPublishRes res = new GetNewPublishRes();
            res.setId(bsNov.getId());
            res.setCover(bsNov.getCover());
            res.setIntroduction(bsNov.getIntroduction());
            res.setNovelName(bsNov.getNovelname());
            list.add(res);
        }
        return list;
    }

    /**
     * 获取推荐
     * @param claims
     * @return
     */
    @Override
    @SuppressWarnings("all")
    public List<GetNewPublishRes> getRecommend(Claims claims) {
        Integer userId = (Integer) claims.get("userId");
        BsUser bsUser = userMapper.selectByPrimaryKey(userId);
        String[] like = bsUser.getLike().split(",");
        //通过喜好获取三个推荐
        List<GetNewPublishRes> list = new ArrayList<>();
        for (String str: like) {
            List<BsNov> bsNovList = novMapper.selectBySortRun(str,3);
            log.info("str="+str+bsNovList.toString());
            for (BsNov bsNov:bsNovList){
                log.info("str="+str+bsNov.getId()+"name="+bsNov.getNovelname());
                GetNewPublishRes res = new GetNewPublishRes();
                res.setIntroduction(bsNov.getIntroduction());
                res.setId(bsNov.getId());
                res.setNovelName(bsNov.getNovelname());
                res.setCover(bsNov.getCover());
                list.add(res);
            }
        }
        return list;
    }


    /**
     * 3.6 由用户足迹获取个性化推荐
     * @param claims
     * @return
     */
    @Override
    @SuppressWarnings("all")
    public List<GetNewPublishRes> getRecommendByHistory(Claims claims) {
        Integer userId = (Integer) claims.get("userId");
        BsTraceExample example = new BsTraceExample();
        BsTraceExample.Criteria criteria = example.createCriteria();
        criteria.andUidEqualTo(userId);
        List<BsTrace> bsTraces = traceMapper.selectByExample(example);
        if (CommonUtils.isEmpty(bsTraces)){
            return new ArrayList<>();
        }
        BsTrace trace = bsTraces.get(0);
        String[] split = trace.getSort().split(",");
        List<GetNewPublishRes> list = new ArrayList<>();
        for (String str: Arrays.asList(split)) {
            List<BsNov> bsNovList = novMapper.selectBySortRun(str,2);
            for (BsNov bsNov:bsNovList){
                log.info("str="+str+bsNov.getId()+"name="+bsNov.getNovelname());
                GetNewPublishRes res = new GetNewPublishRes();
                res.setIntroduction(bsNov.getIntroduction());
                res.setId(bsNov.getId());
                res.setNovelName(bsNov.getNovelname());
                res.setCover(bsNov.getCover());
                res.setSort(bsNov.getSort());
                list.add(res);
            }
        }
        return list;
    }

    @Override
    public int modifyNovel(Map<String, String> maps) {
        BsNov bsNov = new BsNov();
        String id = maps.get("id");
        String novelName = maps.get("novelName");
        String introduction = maps.get("introduction");
        String sort = maps.get("sort");
        String cover = maps.get("cover");
        String price = maps.get("price");
        String time = maps.get("time");

        if (id==null) return 0; else bsNov.setId(Integer.valueOf(id));
        if (novelName==null) ; else bsNov.setNovelname(novelName);
        if (introduction==null) ; else bsNov.setIntroduction(introduction);
        if (sort==null) ; else bsNov.setSort(sort);
        if (cover==null) ; else bsNov.setCover(cover);
        if (price==null) ; else bsNov.setPrice(Integer.valueOf(price));
        if (time==null) ; else bsNov.setTime(time);

        int status = novMapper.updateByPrimaryKeySelective(bsNov);
        return status;
    }

    @Override
    public List<GetNewPublishRes> getSearch(String searchWay, String key) {
        BsNovExample example = new BsNovExample();
        BsNovExample.Criteria criteria = example.createCriteria();
        if (searchWay.equals("book_name")){
            criteria.andNovelnameLike("%"+key+"%");
        } else {
            criteria.andEditornameLike("%"+key+"%");
        }
        List<GetNewPublishRes> list = new ArrayList<>();
        List<BsNov> bsNovList = novMapper.selectByExample(example);
        for (BsNov bsNov:bsNovList){
            GetNewPublishRes res = new GetNewPublishRes();
            res.setIntroduction(bsNov.getIntroduction());
            res.setId(bsNov.getId());
            res.setNovelName(bsNov.getNovelname());
            res.setCover(bsNov.getCover());
            res.setSort(bsNov.getSort());
            list.add(res);
        }
        return list;
    }

    @Override
    public GetNovelInfoRes getNovelInfo(Integer id) {
        BsNov bsNov = novMapper.selectByPrimaryKey(id);
        GetNovelInfoRes res = new GetNovelInfoRes(bsNov.getNovelname(),bsNov.getEditorname(),bsNov.getEditorid()
        ,bsNov.getIntroduction(),bsNov.getClicknum(),bsNov.getCommentnum(), bsNov.getDownloadnum(),bsNov.getSort()
        , bsNov.getPrice(),bsNov.getCover(),bsNov.getTime());
        return res;
    }

    /**
     * 用户购买小说
     * 从用户身上扣钱
     * 作者身价价钱
     * @param novId
     * @param claims
     * @return
     */
    @Override
    @Transactional
    public Integer buyNovel(Integer novId, Claims claims) {
        Integer userId = (Integer) claims.get("userId");
        BsNov bsNov = novMapper.selectByPrimaryKey(novId);
        //找到作者用户
        BsUser editer = userMapper.selectByPrimaryKey(bsNov.getEditorid());
        //用户id
        BsUser user = userMapper.selectByPrimaryKey(userId);
        BsUser editer_update = new BsUser();
        editer_update.setId(editer.getId());
        editer_update.setVp(editer.getVp()+bsNov.getPrice());
        BsUser user_update = new BsUser();
        user_update.setId(user.getId());
        user_update.setVp(user.getVp()-bsNov.getPrice());
        if (user_update.getVp() <= 0){
            //钱不够
            return -1;
        }
        //检查是否已经存在
        BsUserFavExample example = new BsUserFavExample();
        BsUserFavExample.Criteria criteria = example.createCriteria();
        criteria.andUidEqualTo(userId);
        criteria.andNovelidEqualTo(novId);
        criteria.andStatusEqualTo(1);
        List<BsUserFav> bsUserFavs = favMapper.selectByExample(example);
        if (CommonUtils.isNotEmpty(bsUserFavs)){
            //已经购买
            return -2;
        }
        //添加记录并更新
        favMapper.insertSelective(new BsUserFav(userId,novId,1));
        int status1 = userMapper.updateByPrimaryKeySelective(editer_update);
        int status2 = userMapper.updateByPrimaryKeySelective(user_update);
        if (status1 <= 0 || status2 <= 0 ) return 0;
        return 1;
    }


    /**
     * 小说下载数量加一
     * @param cId
     * @return
     */
    @Override
    @Transactional
    public BsChapter download(Integer cId) {
        BsChapter chapter = chapterMapper.selectByPrimaryKey(cId);
        //去除标签算法
        chapter.setContent(editHtml(chapter.getContent()));
        Integer novId = chapter.getNovelid();
        BsNov bsNov = novMapper.selectByPrimaryKey(novId);
        bsNov.setDownloadnum(bsNov.getDownloadnum()+1);
        novMapper.updateByPrimaryKeySelective(bsNov);
        return chapter;
    }

    @Override
    public GetChapterInfoRes getChapterInfo(Integer novelId, Integer chapter) {
        BsChapterExample example = new BsChapterExample();
        BsChapterExample.Criteria criteria = example.createCriteria();
        criteria.andNovelidEqualTo(novelId);
        criteria.andChapterEqualTo(chapter);
        List<BsChapter> bsChapters = chapterMapper.selectByExampleWithBLOBs(example);
        if (CommonUtils.isEmpty(bsChapters)){
            return null;
        }
        GetChapterInfoRes res = new GetChapterInfoRes();
        res.setId(bsChapters.get(0).getId());
        res.setChapter(bsChapters.get(0).getChapter());
        res.setContent(bsChapters.get(0).getContent());
        res.setNovelId(bsChapters.get(0).getNovelid());
        res.setTitle(bsChapters.get(0).getTitle());
        return res;
    }

    @Override
    public List<GetCollectionRes> getBought(Claims claims) {
        Integer userId = (Integer) claims.get("userId");
        BsUserFavExample example = new BsUserFavExample();
        BsUserFavExample.Criteria criteria = example.createCriteria();
        criteria.andUidEqualTo(userId);
        criteria.andStatusEqualTo(1);
        List<BsUserFav> bsUserFavs = favMapper.selectByExample(example);
        List<GetCollectionRes> list = new ArrayList<>();
        for (BsUserFav fav:bsUserFavs){
            GetCollectionRes res = new GetCollectionRes();
            res.setId(fav.getId());
            res.setNovelId(fav.getNovelid());
            BsNov bsNov = novMapper.selectByPrimaryKey(fav.getNovelid());
            if (CommonUtils.isEmpty(bsNov)){
                continue;
            }
            res.setNovelName(bsNov.getNovelname());
            res.setIntroduction(bsNov.getIntroduction());
            res.setCover(bsNov.getCover());
            list.add(res);
        }
        return list;
    }

    @Override
    public GetNovelDataRes getNovelData(Integer novId) {
        GetNovelDataRes res = new GetNovelDataRes();
        BsNov bsNov = novMapper.selectByPrimaryKey(novId);
        res.setClick(bsNov.getClicknum());
        res.setComment(bsNov.getCommentnum());
        res.setDownload(bsNov.getDownloadnum());
        BsUserFavExample example = new BsUserFavExample();
        BsUserFavExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo(0);
        criteria.andNovelidEqualTo(novId);
        int collection = favMapper.countByExample(example);
        res.setCollection(collection);
        return res;
    }



    /**
     * 去除变迁 碰见< 寻找到下一个 >  匹配到就删除
     * 格式必选满足 成对出现的格式 否则会错误或者误删
     * @param content
     * @return
     */
    public String editHtml(String content){
        int start = -1;
        int end = -1;
        do {
            start = content.indexOf("<");
            end =content.indexOf(">");
            if (start >= 0 && end >= 0)
                content = content.substring(0,start) + content.substring(end+1);
        } while (start >= 0 && end >= 0);
        return content;
    }


    @SuppressWarnings("all")
    @Autowired
    private BsTraceMapper traceMapper;

    @SuppressWarnings("all")
    @Autowired
    private BsUserFavMapper favMapper;

    @SuppressWarnings("all")
    @Autowired
    private BsChapterMapper chapterMapper;

    @SuppressWarnings("all")
    @Autowired
    private BsNovMapper novMapper;

    @SuppressWarnings("all")
    @Autowired
    private BsUserMapper userMapper;
}
