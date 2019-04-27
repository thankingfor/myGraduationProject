package vip.bzsy.novelshop.service;

import io.jsonwebtoken.Claims;
import vip.bzsy.novelshop.model.BsUser;
import vip.bzsy.novelshop.model.vo.GetUserListRes;
import vip.bzsy.novelshop.model.vo.RegisterReq;

import java.util.List;

/**
 * @author lyf
 * @create 2019-03-11 16:48
 */
public interface UserService {

    int register(RegisterReq registerReq);

    BsUser selectByAccount(String account);

    Integer recharge(Claims claims, Integer vp);

    Integer signing(Claims claims);

    List<BsUser> getUserList(Integer ban, Claims claims);

    Integer managerUser(Integer userId, String action);

    Integer modifyUserInfo(BsUser user);

    List<BsUser> searchUser(String name);
}
