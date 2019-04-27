package vip.bzsy.novelshop.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vip.bzsy.novelshop.common.Audience;
import vip.bzsy.novelshop.common.CommonResponse;
import vip.bzsy.novelshop.common.CommonStatus;
import vip.bzsy.novelshop.model.BsChapter;
import vip.bzsy.novelshop.model.vo.*;
import vip.bzsy.novelshop.service.NovService;
import vip.bzsy.novelshop.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author lyf
 * @create 2019-03-12 17:11
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/nov" ,produces = "application/json;charset=UTF-8")
public class NovController {

    @RequestMapping(value = "/modifyNovel",method = RequestMethod.POST)
    public CommonResponse modifyNovel(@RequestBody Map<String,String> maps){
        int status = novService.modifyNovel(maps);
        if (status <=0 ){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    @RequestMapping(value = "/getNovelData",method = RequestMethod.GET)
    public CommonResponse getNovelData(@RequestParam("id") Integer novId){
        GetNovelDataRes res = novService.getNovelData(novId);
        return CommonResponse.success(res);
    }

    @RequestMapping(value = "/getBought",method = RequestMethod.GET)
    public CommonResponse getBought(HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        List<GetCollectionRes> res = novService.getBought(claims);
        return CommonResponse.success(res);
    }

    @RequestMapping(value = "/getChapterInfo",method = RequestMethod.GET)
    public CommonResponse getChapterInfo(Integer novelId, Integer chapter){
        GetChapterInfoRes res = novService.getChapterInfo(novelId,chapter);
        return CommonResponse.success(res);
    }

    @RequestMapping(value = "/download",method = RequestMethod.GET)
    public void download2(@RequestParam("id") Integer cId, HttpServletRequest request,
                                   HttpServletResponse response) throws Exception {
        BsChapter chapter = novService.download(cId);
        String content = chapter.getContent().trim().replaceAll("<p>","").replaceAll("</p>","");
        //response下载
        String uuid = UUID.randomUUID().toString().replace("-","");
        String fileName = uuid+chapter.getTitle()+".txt";
        response.setHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(fileName, "utf-8"));
        response.setContentType("multipart/form-data");
        OutputStream ops = null;
        ByteArrayInputStream fis = null;
        byte[] buffer = new byte[8192];
        int bytesRead = 0;
        try {
            ops = response.getOutputStream();
            fis = new ByteArrayInputStream(content.getBytes());
            while((bytesRead = fis.read(buffer, 0, 8192)) != -1){
                ops.write(buffer, 0, bytesRead);
            }
            ops.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(fis != null){
                    fis.close();
                }
                if(ops != null){
                    ops.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
       // return CommonResponse.success();
    }

    @RequestMapping(value = "/buyNovel",method = RequestMethod.GET)
    public CommonResponse buyNovel(@RequestParam("id") Integer novId, HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        Integer status = novService.buyNovel(novId,claims);
        if (status <= 0){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    @RequestMapping(value = "/getNovelInfo",method = RequestMethod.GET)
    public CommonResponse getNovelInfo(Integer id){
        GetNovelInfoRes res = novService.getNovelInfo(id);
        return CommonResponse.success(res);
    }

    @RequestMapping(value = "/getSearch",method = RequestMethod.GET)
    public CommonResponse getSearch(String searchWay, String key){
        List<GetNewPublishRes> list = novService.getSearch(searchWay,key);
        return CommonResponse.success(list);
    }

    @RequestMapping(value = "/getRecommend",method = RequestMethod.GET)
    public CommonResponse getRecommend(HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        List<GetNewPublishRes> list = novService.getRecommend(claims);
        return CommonResponse.success(list);
    }

    /**
     * 3.6 由用户足迹获取个性化推荐
     * @param request
     * @return
     */
    @RequestMapping(value = "/getRecommendByHistory",method = RequestMethod.GET)
    public CommonResponse getRecommendByHistory(HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        List<GetNewPublishRes> list = novService.getRecommendByHistory(claims);
        return CommonResponse.success(list);
    }

    @RequestMapping(value = "/getRanking",method = RequestMethod.GET)
    public CommonResponse getRanking(String sort){
        PageHelper.startPage(1,9);
        List<GetNewPublishRes> list = novService.getRanking(sort);
        return CommonResponse.success(list);
    }


    @RequestMapping(value = "/getNewPublish",method = RequestMethod.GET)
    public CommonResponse getNewPublish(@RequestParam(defaultValue = "1") Integer page){
        PageHelper.startPage(page,9);
        List<GetNewPublishRes> list = novService.getNewPublish();
        PageInfo<GetNewPublishRes> pageInfo = new PageInfo<>(list);
        map.clear();
        map.put("total",pageInfo.getTotal());
        map.put("novelList",list);
        return CommonResponse.success(map);
    }

    @RequestMapping(value = "/deleteDraft",method = RequestMethod.GET)
    public CommonResponse deleteDraft(Integer id){
        Integer status = novService.deleteDraft(id);
        if (status <= 0){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    @RequestMapping(value = "/saveChapterToDraft",method = RequestMethod.POST)
    public CommonResponse saveChapterToDraft(@RequestBody BsChapter chapter){
        Integer status = novService.saveChapterToDraft(chapter);
        if (status <= 0){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    @RequestMapping(value = "/getDraft",method = RequestMethod.GET)
    public CommonResponse getDraft(Integer id){
        GetDraftRes res = novService.getDraft(id);
        return CommonResponse.success(res);
    }

    @RequestMapping(value = "/addChapterToDraft",method = RequestMethod.POST)
    public CommonResponse addChapterToDraft(@RequestBody AddChapterToDraftReq req){
        Integer status = novService.addChapterToDraft(req);
        if (status <= 0){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }


    @RequestMapping(value = "/getDraftBox",method = RequestMethod.GET)
    public CommonResponse getDraftBox(HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        List<FetDraftBoxRes> list = novService.getDraftBox(claims);
        return CommonResponse.success(list);
    }


    @RequestMapping(value = "/addChapter",method = RequestMethod.POST)
    public CommonResponse addChapter(@RequestBody AddChapterReq req, HttpServletRequest request) throws Exception{
        log.info(req.toString());
        Integer status = novService.addChapter(req);
        if (status <= 0){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    @RequestMapping(value = "/uploadChapter",method = RequestMethod.POST)
    public CommonResponse uploadChapter(UploadChapterReq req, HttpServletRequest request) throws Exception{
        Integer cId = novService.uploadChapter(req);
        map.clear();
        map.put("id",cId);
        return CommonResponse.success(map);
    }

    @RequestMapping(value = "/getNovelChapter",method = RequestMethod.GET)
    public CommonResponse getNovelChapter(Integer id, HttpServletRequest request){
        List<GetNovelChapterRes> list = novService.getNovelChapter(id);
        return CommonResponse.success(list);
    }

    @RequestMapping(value = "/deleteNovel",method = RequestMethod.GET)
    public CommonResponse deleteNovel(Integer id, HttpServletRequest request){
        int status = novService.deleteNovel(id);
        if (status <= 0){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    @RequestMapping(value = "/getPublishedNovel",method = RequestMethod.GET)
    public CommonResponse getPublishedNovel(HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        List<GetPublishedNovelRes> list = novService.getPublishedNovel((Integer)claims.get("userId"));
        return CommonResponse.success(list);
    }

    @RequestMapping(value = "/publishNovel",method = RequestMethod.POST)
    public CommonResponse publishNovel(@RequestBody PublishNovelReq req, HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        int status = novService.publishNovel(req,claims);
        if (status <= 0){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    @Autowired
    private NovService novService;

    @Autowired
    private UserService userService;

    @Autowired
    private Audience audience;

    private Map<String,Object> map = new HashMap<String,Object>();
}
