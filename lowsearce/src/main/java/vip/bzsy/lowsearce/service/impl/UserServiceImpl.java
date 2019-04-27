package vip.bzsy.lowsearce.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.bzsy.lowsearce.common.CommonStatus;
import vip.bzsy.lowsearce.common.JsonReturn;
import vip.bzsy.lowsearce.mapper.UserMapper;
import vip.bzsy.lowsearce.model.FeedBack;
import vip.bzsy.lowsearce.model.User;
import vip.bzsy.lowsearce.model.vo.FeedBackVO;
import vip.bzsy.lowsearce.model.vo.SearchUserRequest;
import vip.bzsy.lowsearce.model.vo.SearchUserResponse;
import vip.bzsy.lowsearce.service.UserService;
import vip.bzsy.lowsearce.util.BzsyUtils;

import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService{

    @Autowired
    private UserMapper userMapper;

    @Override
    public User selectById(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    public User selectByAccount(String account) {
        return userMapper.selectByAccount(account);
    }

    @Override
    public int add(User user) {
        return userMapper.add(user);
    }

    @Override
    public int updatePwd(String newPwd,Integer id) {
        return userMapper.updatePwd(newPwd,id);
    }

    @Override
    public void updateLoginCount(User user1) {
        user1.setLoginCount(user1.getLoginCount()+1);
        updateUser(user1);
    }

    @Override
    public int updateUser(User user) {
        return userMapper.updateUser(user);
    }

    /**
     * 是否被注册
     * 目前只检查account
     * ture 没被注册
     * false被注册
     * @param user
     * @return
     */
    @Override
    public boolean check(User user) {
        User user1 = selectByAccount(user.getAccount());
        if (BzsyUtils.isNotEmpty(user1)){
            return false;
        }
        return true;
    }

    @Override
    public JsonReturn addFeedBack(FeedBack feedBack) {
        int status = userMapper.addFeedBack(feedBack);
        if (status <= 0){
            JsonReturn.fail(CommonStatus.EXCEPTION);
        }
        return JsonReturn.success();
    }

    @Override
    public List<SearchUserResponse> searchUser(SearchUserRequest searchUserRequest) {
        //换个方法
        //List<SearchUserResponse> list = userMapper.searchUser(searchUserRequest);
        List<SearchUserResponse> userList = userMapper.searchUSER(searchUserRequest);
        for (SearchUserResponse userResponse: userList) {
            List<FeedBackVO> vo =  userMapper.findBackByAccount(userResponse.getAccount());
            userResponse.setFeedback(vo);
        }
        log.info(userList.toString());
        return userList;
    }

    @Override
    public int deleteUser(Integer userId) {
        return userMapper.deleteUser(userId);
    }

}
