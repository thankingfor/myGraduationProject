package vip.bzsy.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import vip.bzsy.common.*;
import vip.bzsy.model.Faculity;
import vip.bzsy.model.User;
import vip.bzsy.model.vo.StudentInfoRes;
import vip.bzsy.model.vo.UserInfoRes;
import vip.bzsy.service.ClassroomService;
import vip.bzsy.service.FaculityService;
import vip.bzsy.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lyf
 * @since 2019-03-22
 */
@Slf4j
@RestController
@RequestMapping(value = "/api" ,produces = "application/json;charset=UTF-8")
public class UserController {

    @RequestMapping(value = "/deleteUser",method = RequestMethod.GET)
    public CommonResponse deleteUser(@RequestParam(value = "id") Integer userId){
        userService.removeById(userId);
        return CommonResponse.success();
    }

    @RequestMapping(value = "/getTeacherData",method = RequestMethod.GET)
    public CommonResponse getTeacherData(@RequestParam(defaultValue = "1") Integer page,
                                         @RequestParam(defaultValue = "10") Integer rows,
                                         @RequestParam(defaultValue = "") String param){
        IPage<User> listPage = userService.page(new Page<User>(page, rows),
                new QueryWrapper<User>()
                        .eq("identity", "teacher")
                        .like("name", param));
        map.clear();
        map.put("total",listPage.getTotal());
        List<StudentInfoRes> list = new ArrayList<>();
        for (User user:listPage.getRecords()){
            StudentInfoRes res = new StudentInfoRes();
            BeanUtils.copyProperties(user,res);
            list.add(res);
        }
        map.put("teacherData",list);
        return CommonResponse.success(map);
    }

    @RequestMapping(value = "/getStudentData",method = RequestMethod.GET)
    public CommonResponse getStudentData(@RequestParam(defaultValue = "1") Integer page,
                                         @RequestParam(defaultValue = "10") Integer rows,
                                         @RequestParam(defaultValue = "") String param){
        IPage<User> listPage = userService.page(new Page<User>(page, rows),
                new QueryWrapper<User>()
                        .eq("identity", "student")
                        .like("name", param));
        map.clear();
        map.put("total",listPage.getTotal());
        List<StudentInfoRes> list = new ArrayList<>();
        for (User user:listPage.getRecords()){
            StudentInfoRes res = new StudentInfoRes();
            BeanUtils.copyProperties(user,res);
            res.setFaculity(new Faculity().setId(user.getFaculityId()).selectById().getName());
            list.add(res);
        }
        map.put("studentData",list);
        return CommonResponse.success(map);
    }

    @RequestMapping(value = "/getSchoolInfo",method = RequestMethod.GET)
    public CommonResponse getStudent(){
        int classroomNum = classroomService.count();
        int studentNum = userService.count(new QueryWrapper<User>().eq("identity","student"));
        int teacherNum = userService.count(new QueryWrapper<User>().eq("identity","teacher"));
        map.clear();
        map.put("studentNum",studentNum);
        map.put("teacherNum",teacherNum);
        map.put("classroomNum",classroomNum);
        return CommonResponse.success(map);
    }

    @RequestMapping(value = "/userInfo",method = RequestMethod.GET)
    public CommonResponse userInfo(HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        Integer userId = (Integer) claims.get("userId");
        User byId = userService.getById(userId);
        UserInfoRes res = new UserInfoRes();
        res.setUid(byId.getId());
        BeanUtils.copyProperties(byId,res);
        Faculity faculity = faculityService.getById(byId.getFaculityId());
        res.setFaculity(faculity.getName());
        if (byId.getIdentity().equals("student")){
            //判断学期 1-8
            int semester=  userService.getSemester(byId.getEnterTime());
            res.setSemester(semester);
        }
        return CommonResponse.success(res);
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public CommonResponse login(@RequestBody User user){
        User one = userService.getOne(new QueryWrapper<User>()
                .eq("account", user.getAccount())
                .eq("password", Md5Utils.encryptPassword(user.getPassword(), SALT, PASS_COUNT)));
        if (CommonUtils.isEmpty(one)){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        Faculity faculity = faculityService.getById(one.getFaculityId());
        //jwt操作
        map.clear();
        String jewToken = JwtHelper.createJWT(one.getId(),one.getAccount(),one.getName(),one.getIdentity(),
                faculity.getId(),faculity.getName(),one.getEnterTime(),
                audience.getClientId(), audience.getName(),audience.getExpiresSecond(), audience.getBase64Secret());
        map.put("token",jewToken);
        return CommonResponse.success(map);
    }

    @RequestMapping(value = "/addTeacher",method = RequestMethod.POST)
    public CommonResponse addTeacher(@RequestBody User user){
        user.setIdentity("teacher");
        boolean save = userService.register(user);
        if (!save){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public CommonResponse register(@RequestBody User user){
        user.setIdentity("student");
        boolean save = userService.register(user);
        if (!save){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    private Map<String,Object> map = new LinkedHashMap<>();


    @Autowired
    private Audience audience;

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private FaculityService faculityService;

    @Autowired
    private UserService userService;

    @Value("${SALT}")
    private String SALT;

    @Value("${PASS_COUNT}")
    private Integer PASS_COUNT;
}

