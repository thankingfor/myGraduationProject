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
import java.util.Arrays;
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
     * 3.5 上传用户足迹
     * @param sort
     * @param userId
     */
    @Override
    public void postHistory(String sort, Integer userId) {
        BsTraceExample example = new BsTraceExample();
        BsTraceExample.Criteria criteria = example.createCriteria();
        criteria.andUidEqualTo(userId);
        List<BsTrace> bsTraces = traceMapper.selectByExample(example);
        if (CommonUtils.isEmpty(bsTraces)){
            BsTrace trace = new BsTrace();
            trace.setSort(sort);
            trace.setUid(userId);
            traceMapper.insertSelective(trace);
        } else {
            BsTrace trace = bsTraces.get(0);
            String sort1 = trace.getSort();
            String[] split = sort1.split(",");
            //判断是否重复
            boolean contains = Arrays.asList(split).contains(sort);
            if (!contains){
                //添加
                if (split.length > 2){
                    sort1 = split[1]+","+split[2]+","+sort;
                } else {
                    sort1 = sort1 + "," +sort;
                }
                trace.setSort(sort1);
                traceMapper.updateByPrimaryKeySelective(trace);
            }
        }
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
