package vip.bzsy.lowsearce.service;

import vip.bzsy.lowsearce.common.JsonReturn;
import vip.bzsy.lowsearce.model.FeedBack;
import vip.bzsy.lowsearce.model.User;
import vip.bzsy.lowsearce.model.vo.SearchUserRequest;
import vip.bzsy.lowsearce.model.vo.SearchUserResponse;

import java.util.List;

public interface UserService {

    User selectById(Integer id);

    User selectByAccount(String account);

    int add(User user);

    int updatePwd(String newPwd ,Integer id);

    void updateLoginCount(User user);

    int updateUser(User user);

    boolean check(User user);

    JsonReturn addFeedBack(FeedBack feedBack);

    List<SearchUserResponse> searchUser(SearchUserRequest searchUserRequest);

    int deleteUser(Integer userId);
}
