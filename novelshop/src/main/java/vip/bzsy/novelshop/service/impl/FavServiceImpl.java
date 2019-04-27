package vip.bzsy.novelshop.service.impl;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.bzsy.novelshop.common.CommonUtils;
import vip.bzsy.novelshop.mapper.*;
import vip.bzsy.novelshop.model.*;
import vip.bzsy.novelshop.model.vo.*;
import vip.bzsy.novelshop.service.FavService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lyf
 * @create 2019-03-13 18:54
 */
@Service
@Slf4j
public class FavServiceImpl implements FavService {


    @Override
    public List<GetCollectionRes> getCollection(Claims claims) {
        Integer userId = (Integer) claims.get("userId");
        List<BsUserFav> listFavByUserIdAndStatus = getListFavByUserIdAndStatus(userId, 0);
        List<GetCollectionRes> list = new ArrayList<>();
        for (BsUserFav fav : listFavByUserIdAndStatus){
            GetCollectionRes res = new GetCollectionRes();
            res.setId(fav.getId());
            res.setNovelId(fav.getNovelid());
            BsNov bsNov = novMapper.selectByPrimaryKey(fav.getNovelid());
            res.setCover(bsNov.getCover());
            res.setIntroduction(bsNov.getIntroduction());
            res.setNovelName(bsNov.getNovelname());
            list.add(res);
        }
        return list;
    }

    @Override
    public int collection(Claims claims, Integer novId) {
        Integer userId = (Integer) claims.get("userId");
        BsUserFav fav = new BsUserFav();
        fav.setNovelid(novId);
        fav.setStatus(0);
        fav.setUid(userId);
        return favMapper.insertSelective(fav);
    }

    @Override
    public int deleteCollection(Integer id) {
        return favMapper.deleteByPrimaryKey(id);
    }

    /**
     * 已购买为1，未购买为0
     * @param novelId
     * @param claims
     * @return
     */
    @Override
    public int getStatus(Integer novelId, Claims claims) {
        Integer userId = (Integer) claims.get("userId");
        BsUserFavExample example = new BsUserFavExample();
        BsUserFavExample.Criteria criteria = example.createCriteria();
        criteria.andUidEqualTo(userId);
        criteria.andStatusEqualTo(1);
        criteria.andNovelidEqualTo(novelId);
        List<BsUserFav> bsUserFavs = favMapper.selectByExample(example);
        if (CommonUtils.isNotEmpty(bsUserFavs))
            return 1;
        return 0;
    }

    @Override
    @Transactional
    public int getCollectionStatus(Integer id, Claims claims) {
        log.info(id.toString());
        Integer userId = (Integer) claims.get("userId");
        BsUserFavExample example = new BsUserFavExample();
        BsUserFavExample.Criteria criteria = example.createCriteria();
        criteria.andUidEqualTo(userId);
        criteria.andStatusEqualTo(0);
        criteria.andNovelidEqualTo(id);
        BsNov bsNov = novMapper.selectByPrimaryKey(id);
        bsNov.setClicknum(bsNov.getClicknum()+1);
        novMapper.updateByPrimaryKeySelective(bsNov);
        List<BsUserFav> bsUserFavs = favMapper.selectByExample(example);
        if (CommonUtils.isNotEmpty(bsUserFavs))
            return 1;
        return 0;
    }

    @Override
    public int deleteCollection(Integer novId, Claims claims,Integer status) {
        Integer userId = (Integer) claims.get("userId");
        BsUserFavExample example = new BsUserFavExample();
        BsUserFavExample.Criteria criteria = example.createCriteria();
        criteria.andUidEqualTo(userId);
        criteria.andStatusEqualTo(status);
        criteria.andNovelidEqualTo(novId);
        return favMapper.deleteByExample(example);
    }

    /**
     * 发布评论
     * 小说评论数+1
     * @param req
     * @param claims
     * @return
     */
    @Override
    @Transactional
    public int publishComment(PublishCommentReq req, Claims claims) {
        Integer userId = (Integer) claims.get("userId");
        BsNov bsNov = novMapper.selectByPrimaryKey(req.getNovelId());
        if (CommonUtils.isEmpty(bsNov)) return 0;
        bsNov.setCommentnum(bsNov.getCommentnum()+1);
        int i = novMapper.updateByPrimaryKeySelective(bsNov);
        if (i <= 0) return 0;
        BsMessage message = new BsMessage();
        message.setContent(req.getContent());
        message.setTime(req.getTime());
        message.setNovelid(req.getNovelId());
        message.setUid(userId);
        return messageMapper.insertSelective(message);
    }

    @Override
    public List<GetCommentRes> getComment(Integer novelId) {
        return messageMapper.getComment(novelId);
    }

    @Override
    public Integer customerService(BsBack bsBack, Claims claims) {
        Object userId = claims.get("userId");
        if (CommonUtils.isNotEmpty(userId)){
            bsBack.setUid((Integer) userId);
        }
        bsBack.setCreated(new Date().getTime());
        return backMapper.insertSelective(bsBack);
    }

    @Override
    public Integer deleteComment(Integer id) {
        return messageMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Integer reportComment(Integer id) {
        BsMessage message = messageMapper.selectByPrimaryKey(id);
        message.setIsBan(message.getIsBan()+1);
        return messageMapper.updateByPrimaryKeySelective(message);
    }

    @Override
    public List<GetRepotedCommentRes> getRepotedComment() {
        BsMessageExample example = new BsMessageExample();
        BsMessageExample.Criteria criteria = example.createCriteria();
        example.setOrderByClause("time desc");
        criteria.andIsBanNotEqualTo(0);
        List<BsMessage> bsMessages = messageMapper.selectByExample(example);
        List<GetRepotedCommentRes> resList = new ArrayList<>();
        for (BsMessage message : bsMessages){
            GetRepotedCommentRes res = new GetRepotedCommentRes();
            res.setId(message.getId());
            res.setUid(message.getUid());
            res.setContent(message.getContent());
            BsUser bsUser = userMapper.selectByPrimaryKey(message.getUid());
            if(CommonUtils.isEmpty(bsUser)){
                res.setName("用户不存在");
            }else {
                res.setName(bsUser.getName());
            }
            resList.add(res);
        }
        return resList;
    }

    @Override
    public List<GetFeedBackRes> getFeedback() {
        List<GetFeedBackRes> resList = new ArrayList<>();
        /*BsBackExample example = new BsBackExample();*/
        List<BsBack> bsBacks = backMapper.selectByExample(null);
        for (BsBack b : bsBacks){
            GetFeedBackRes res = new GetFeedBackRes();
            res.setContent(b.getContent());
            res.setEmail(b.getEmail());
            res.setId(b.getId());
            resList.add(res);
        }
        return resList;
    }


    /**
     * 通过用户id和状态返回fav对象
     * @param userId
     * @param status
     * @return
     */
    public List<BsUserFav> getListFavByUserIdAndStatus(Integer userId,Integer status){
        BsUserFavExample example = new BsUserFavExample();
        BsUserFavExample.Criteria criteria = example.createCriteria();
        criteria.andUidEqualTo(userId);
        criteria.andStatusEqualTo(status);
        List<BsUserFav> bsUserFavs = favMapper.selectByExample(example);
        return bsUserFavs;
    }


    @SuppressWarnings("all")
    @Autowired
    private BsBackMapper backMapper;

    @SuppressWarnings("all")
    @Autowired
    private BsMessageMapper messageMapper;

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
