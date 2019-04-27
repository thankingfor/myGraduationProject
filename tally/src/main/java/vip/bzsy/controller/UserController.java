package vip.bzsy.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import vip.bzsy.common.*;
import vip.bzsy.model.User;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lyf
 * @since 2019-03-27
 */
@Slf4j
@RestController
@RequestMapping(value = "/api" ,produces = "application/json;charset=UTF-8")
public class UserController extends BaseController {

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public CommonResponse register(@RequestBody User user){
        //查询是否重复
        User selectOne = user.selectOne(new QueryWrapper<User>().eq("account",user.getAccount()));
        if (CommonUtils.isNotEmpty(selectOne)){
            return CommonResponse.fail("账户已经存在");
        }
        //密码加密
        user.setPassword(Md5Utils.encryptPassword(user.getPassword(),SALT,PASS_COUNT));
        boolean save = userService.save(user);
        if (!save){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public CommonResponse login(@RequestBody User user){
        User one = userService.getOne(new QueryWrapper<User>()
                .eq("account", user.getAccount())
                .eq("password", Md5Utils.encryptPassword(user.getPassword(), SALT, PASS_COUNT)));
        if (CommonUtils.isEmpty(one)){
            return CommonResponse.fail(CommonStatus.USER_LOGIN_ACCOUNT_FAULT);
        }
        //jwt操作
        map.clear();
        String jewToken = JwtHelper.createJWT(one.getId(),
                audience.getClientId(), audience.getName(),audience.getExpiresSecond(), audience.getBase64Secret());
        map.put("token",jewToken);
        return CommonResponse.success(map);
    }

    @RequestMapping(value = "/getUserInfo",method = RequestMethod.GET)
    public CommonResponse getUserInfo(HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        Integer userId = (Integer) claims.get("userId");
        User user = userService.getById(userId);
        map.clear();
        map.put("uid",user.getId());
        map.put("name",user.getName());
        map.put("account",user.getAccount());
        map.put("regTime",user.getRegTime());
        return CommonResponse.success(map);
    }

}

