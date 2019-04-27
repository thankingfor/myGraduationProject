package vip.bzsy.novelshop.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import vip.bzsy.novelshop.common.*;
import vip.bzsy.novelshop.model.BsUser;
import vip.bzsy.novelshop.model.vo.GetUserListRes;
import vip.bzsy.novelshop.model.vo.RegisterReq;
import vip.bzsy.novelshop.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lyf
 * @create 2019-03-11 16:46
 */
@Slf4j
@RestController
@RequestMapping(value = "/api/nov" ,produces = "application/json;charset=UTF-8")
public class UserController {

    @SuppressWarnings("all")
    @RequestMapping(value = "/searchUser",method = RequestMethod.GET)
    public CommonResponse searchUser(String name ,HttpServletRequest request){
        List<BsUser> list = userService.searchUser(name);
        List<GetUserListRes> listRes = new ArrayList<>();
        for (BsUser user:list){
            GetUserListRes res = new GetUserListRes();
            res.setId(user.getId());
            res.setBan(user.getBan());
            res.setIdentity(user.getIdentity());
            res.setName(user.getName());
            res.setAccount(user.getAccount());
            res.setVp(user.getVp());
            res.setLike(user.getLike().split(","));
            listRes.add(res);
        }
        return CommonResponse.success(listRes);
    }

    @RequestMapping(value = "/modifyUserInfo",method = RequestMethod.POST)
    public CommonResponse modifyUserInfo(@RequestBody BsUser user ,HttpServletRequest request){
        Integer status = userService.modifyUserInfo(user);
        if (status <= 0){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    @RequestMapping(value = "/managerUser",method = RequestMethod.GET)
    public CommonResponse managerUser(@RequestParam(value = "id",defaultValue = "0") Integer userId ,
                                      @RequestParam(value = "action",defaultValue = "9") String action,
                                      HttpServletRequest request){
        Integer status = userService.managerUser(userId,action);
        if (status <= 0){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    @SuppressWarnings("all")
    @RequestMapping(value = "/getUserList",method = RequestMethod.GET)
    public CommonResponse getUserList(@RequestParam(defaultValue = "0") Integer ban ,
                                      @RequestParam(defaultValue = "1") Integer page,
                                      @RequestParam(defaultValue = "9") Integer rows,
                                      HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        PageHelper.startPage(page,rows);
        List<BsUser> list = userService.getUserList(ban,claims);
        PageInfo<BsUser> pageInfo = new PageInfo<>(list);
        List<GetUserListRes> listRes = new ArrayList<>();
        for (BsUser user:pageInfo.getList()){
            GetUserListRes res = new GetUserListRes();
            res.setId(user.getId());
            res.setBan(user.getBan());
            res.setIdentity(user.getIdentity());
            res.setName(user.getName());
            res.setAccount(user.getAccount());
            res.setVp(user.getVp());
            res.setLike(user.getLike().split(","));
            listRes.add(res);
        }
        map.clear();
        map.put("total",pageInfo.getTotal());
        map.put("userList",listRes);
        return CommonResponse.success(map);
    }

    @RequestMapping(value = "/signing",method = RequestMethod.GET)
    public CommonResponse signing(HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        Integer status = userService.signing(claims);
        if (status <= 0){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    @RequestMapping(value = "/recharge",method = RequestMethod.GET)
    public CommonResponse recharge(Integer vp,HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        Integer status = userService.recharge(claims,vp);
        if (status <= 0){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    @RequestMapping(value = "/userInfo",method = RequestMethod.GET)
    public CommonResponse userInfo (HttpServletRequest request){
        map.clear();
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        BsUser bsUser = userService.selectByAccount((String) claims.get("account"));
        if (CommonUtils.isEmpty(bsUser)){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        map.put("uid",bsUser.getId());
        map.put("account",bsUser.getAccount());
        map.put("name",bsUser.getName());
        map.put("identity",bsUser.getIdentity());
        map.put("vp",bsUser.getVp());
        map.put("ban",bsUser.getBan());
        String like = bsUser.getLike();
        String[] likeStr = like.split(",");
        map.put("like",likeStr);
        return CommonResponse.success(map);
    }


    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public CommonResponse login(@RequestBody Map<String,String> user){
        map.clear();
        BsUser bsUser = userService.selectByAccount(user.get("account"));
        if (!bsUser.getPassword().equals(Md5Utils.encryptPassword(user.get("password"),SALT,PASS_COUNT))){
            return CommonResponse.fail("账号或密码错误");
        }
        if (!bsUser.getIdentity().equals(user.get("loginWay"))){
            return CommonResponse.fail("身份不对");
        }
        String jewToken = JwtHelper.createJWT(bsUser.getId(),bsUser.getIdentity(),bsUser.getName(),bsUser.getAccount(),
                audience.getClientId(), audience.getName(),audience.getExpiresSecond(), audience.getBase64Secret());
        map.put("token",jewToken);
        return CommonResponse.success(map);
    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public CommonResponse register(@RequestBody RegisterReq registerReq) {
        int status = userService.register(registerReq);
        if (status <= 0){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    @Autowired
    private UserService userService;

    @Autowired
    private Audience audience;

    private Map<String,Object> map = new HashMap<String,Object>();

    @Value("${SALT}")
    private String SALT;

    @Value("${PASS_COUNT}")
    private Integer PASS_COUNT;
}
