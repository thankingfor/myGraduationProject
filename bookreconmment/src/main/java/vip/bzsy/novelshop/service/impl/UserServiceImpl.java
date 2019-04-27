package vip.bzsy.novelshop.service.impl;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import vip.bzsy.novelshop.common.CommonUtils;
import vip.bzsy.novelshop.common.Md5Utils;
import vip.bzsy.novelshop.mapper.BsUserMapper;
import vip.bzsy.novelshop.model.BsUser;
import vip.bzsy.novelshop.model.BsUserExample;
import vip.bzsy.novelshop.model.vo.GetUserListRes;
import vip.bzsy.novelshop.model.vo.RegisterReq;
import vip.bzsy.novelshop.service.UserService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lyf
 * @create 2019-03-11 16:48
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService{



    @Override
    public int register(RegisterReq registerReq) {
        BsUserExample example = new BsUserExample();
        BsUserExample.Criteria criteria = example.createCriteria();
        criteria.andAccountEqualTo(registerReq.getAccount());
        List<BsUser> bsUsers = userMapper.selectByExample(example);
        if (CommonUtils.isNotEmpty(bsUsers)){
            log.info("123");
            return 0;
        }
        BsUser user = new BsUser();
        user.setAccount(registerReq.getAccount());
        user.setPassword(Md5Utils.encryptPassword(registerReq.getPassword(),SALT ,PASS_COUNT));
        user.setName(registerReq.getName());
        String like = StringUtils.join(registerReq.getLike(),",");
        user.setLike(like);
        user.setIdentity("reader");
        return userMapper.insertSelective(user);
    }

    @Override
    public BsUser selectByAccount(String account) {
        BsUserExample example = new BsUserExample();
        BsUserExample.Criteria criteria = example.createCriteria();
        criteria.andAccountEqualTo(account);
        List<BsUser> bsUsers = userMapper.selectByExample(example);
        if (CommonUtils.isEmpty(bsUsers)){
            return null;
        }
        return bsUsers.get(0);
    }

    @Override
    public Integer recharge(Claims claims, Integer vp) {
        Integer userId = (Integer) claims.get("userId");
        BsUser bsUser = userMapper.selectByPrimaryKey(userId);
        BsUser bsUser1 = new BsUser();
        bsUser1.setId(bsUser.getId());
        bsUser1.setVp(bsUser.getVp()+vp);
        return userMapper.updateByPrimaryKeySelective(bsUser1);
    }

    @Override
    public Integer signing(Claims claims) {
        Integer userId = (Integer) claims.get("userId");
        BsUser bsUser = new BsUser();
        bsUser.setId(userId);
        bsUser.setIdentity("editor");
        return userMapper.updateByPrimaryKeySelective(bsUser);
    }

    @Override
    public List<BsUser> getUserList(Integer ban, Claims claims) {
        BsUserExample example = new BsUserExample();
        BsUserExample.Criteria criteria = example.createCriteria();
        criteria.andBanEqualTo(ban);
        List<BsUser> list = userMapper.selectByExample(example);
        return list;
    }

    @Override
    public Integer managerUser(Integer userId, String action) {
        BsUser bsUser = new BsUser();
        bsUser.setId(userId);
        if (action.equals("ban")){
            bsUser.setBan(1);
            return userMapper.updateByPrimaryKeySelective(bsUser);
        }else if (action.equals("unban")){
            bsUser.setBan(0);
            return userMapper.updateByPrimaryKeySelective(bsUser);
        }else if (action.equals("delete")){
            return userMapper.deleteByPrimaryKey(userId);
        } else {
            return 0;
        }
    }

    @Override
    public Integer modifyUserInfo(BsUser user) {
        return userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public List<BsUser> searchUser(String name) {
        BsUserExample example = new BsUserExample();
        BsUserExample.Criteria criteria = example.createCriteria();
        criteria.andNameLike("%"+name+"%");
        List<BsUser> list = userMapper.selectByExample(example);
        return list;
    }


    @SuppressWarnings("all")
    @Autowired
    private BsUserMapper userMapper;

    @Value("${SALT}")
    private String SALT;

    @Value("${PASS_COUNT}")
    private Integer PASS_COUNT;
}
