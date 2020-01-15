package vip.bzsy.lowsearce.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import vip.bzsy.lowsearce.common.JsonReturn;
import vip.bzsy.lowsearce.model.FeedBack;
import vip.bzsy.lowsearce.model.User;
import vip.bzsy.lowsearce.model.vo.FeedBackVO;
import vip.bzsy.lowsearce.model.vo.SearchUserRequest;
import vip.bzsy.lowsearce.model.vo.SearchUserResponse;

import java.util.List;

@Repository
public interface UserMapper {

    User selectByAccount(String account);

    User selectById(Integer id);

    int add(User user);

    int updatePwd(@Param("newPwd") String newPwd,@Param("id") Integer id);

    int updateUser(User user);

    int addFeedBack(FeedBack feedBack);

    List<SearchUserResponse> searchUser(SearchUserRequest searchUserRequest);

    List<SearchUserResponse> searchUSER(SearchUserRequest searchUserRequest);

    List<FeedBackVO> findBackByAccount(String account);

    int deleteUser(Integer userId);
}
