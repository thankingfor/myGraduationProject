package vip.bzsy.lowsearce.controller;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vip.bzsy.lowsearce.common.Audience;
import vip.bzsy.lowsearce.common.CommonStatus;
import vip.bzsy.lowsearce.common.JsonReturn;
import vip.bzsy.lowsearce.model.FeedBack;
import vip.bzsy.lowsearce.model.User;
import vip.bzsy.lowsearce.model.vo.SearchUserRequest;
import vip.bzsy.lowsearce.model.vo.SearchUserResponse;
import vip.bzsy.lowsearce.service.UserService;
import vip.bzsy.lowsearce.util.BzsyUtils;
import vip.bzsy.lowsearce.util.JsonHelper;
import vip.bzsy.lowsearce.util.JwtHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/law",produces = "application/json;charset=UTF-8")
public class UserController {

    @RequestMapping(value = "/deleteUser" ,method = RequestMethod.GET)
    public JsonReturn deleteUser(@RequestParam(value = "id") Integer userId, HttpServletRequest request){
        int status = userServiceImpl.deleteUser(userId);
        if (status <= 0){
            return JsonReturn.fail(CommonStatus.FAULT);
        }
        return JsonReturn.success();
    }

    @RequestMapping(value = "/searchUser" ,method = RequestMethod.POST)
    public JsonReturn searchUser(@RequestBody SearchUserRequest searchUserRequest, HttpServletRequest request){
        List<SearchUserResponse> list = userServiceImpl.searchUser(searchUserRequest);
        return JsonReturn.success(list);
    }

    @RequestMapping(value = "/commit" ,method = RequestMethod.POST)
    public JsonReturn commit(@RequestBody FeedBack feedBack, HttpServletRequest request){
        Claims claims = (Claims)request.getAttribute("CLAIMS");
        feedBack.setAccount((String) claims.get("account"));
        return userServiceImpl.addFeedBack(feedBack);
    }

    @RequestMapping(value = "/commitAlterPwd" ,method = RequestMethod.POST)
    public String commitReg(@RequestBody Map<String,Object> map,HttpServletRequest request){
        Claims claims = (Claims)request.getAttribute("CLAIMS");
        User user = userServiceImpl.selectById((Integer) claims.get("userid"));
        if (BzsyUtils.isEmpty(user)){
            return JsonReturn.fail(CommonStatus.USER_LOSE).toJson();
        }
        if (!user.getPassword().equals(map.get("oldPwd"))){
            return JsonReturn.fail(CommonStatus.USER_PASSWORD_FAULT).toJson();
        }
        int status = userServiceImpl.updatePwd((String) map.get("newPwd"),(Integer) claims.get("userid"));
        if (status <= 0){
            return JsonReturn.fail(CommonStatus.USER_REGISTER_FAULT).toJson();
        }
        return JsonReturn.success().toJson();
    }

    @RequestMapping(value = "/commitReg" ,method = RequestMethod.POST)
    public String commitReg(@RequestBody User user){
        map.clear();
        //校验用户是否已经被注册 未注册返回true
        if (!userServiceImpl.check(user)){
            return JsonReturn.fail(CommonStatus.USER_REGISTER_ACCOUNT_ISSET).toJson();
        }
        int status =  userServiceImpl.add(user);
        if (status <= 0){
            return JsonReturn.fail(CommonStatus.USER_REGISTER_FAULT).toJson();
        }
        return JsonReturn.success().toJson();
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(@RequestBody User user,HttpServletRequest request){
        map.clear();
        if (BzsyUtils.isEmpty(user.getAccount())){
            return JsonReturn.fail(CommonStatus.USER_LOGIN_ACCOUNT_FAULT).toJson();
        }
        if (BzsyUtils.isEmpty(user.getIdentity())){
            return JsonReturn.fail(CommonStatus.USER_LOGIN_IDENTITY_EMPEY).toJson();
        }
        User user1 = userServiceImpl.selectByAccount(user.getAccount());
        if (!user1.getPassword().equals(user.getPassword())){
            return JsonReturn.fail(CommonStatus.USER_LOGIN_ACCOUNT_FAULT).toJson();
        }
        if (!user1.getIdentity().equals(user.getIdentity())){
            return JsonReturn.fail(CommonStatus.USER_LOGIN_IDENTITY_FAULT).toJson();
        }
        userServiceImpl.updateLoginCount(user1);//登录次数加1
        String jwtToken = JwtHelper.createJWT(
                user1.getName(), user1.getId(),
                user1.getAccount(),user1.getEmail(),user1.getPhone(),user1.getIdentity(),user1.getLoginCount(),
                audience.getClientId(), audience.getName(),
                audience.getExpiresSecond(), audience.getBase64Secret());
        map.put("token",jwtToken);
        return JsonReturn.success(map,CommonStatus.USER_LOGIN_SUCCESS).toJson();
    }

    @RequestMapping(value = "/getUserInfo" ,method = RequestMethod.GET)
    public String getUserInfo(HttpServletRequest request){
        Claims claims = (Claims)request.getAttribute("CLAIMS");
        if (claims == null){
            return JsonReturn.fail(CommonStatus.TOKEN_LOSE).toJson();
        }
        return JsonReturn.success(claims).toJson();
    }


    @RequestMapping(value = "/getName" ,method = RequestMethod.GET)
    public String getName(@RequestParam(name = "account") String account, HttpServletRequest request){
        map.clear();
        User user = userServiceImpl.selectByAccount(account);
        if (user == null){
            return JsonReturn.fail(CommonStatus.USER_LOSE).toJson();
        }
        map.put("name",user.getName());
        return JsonReturn.success(map).toJson();
    }

    @Autowired
    private UserService userServiceImpl;

    @Autowired
    private Audience audience;

    private Map<String,Object> map = new HashMap<String,Object>();

}
