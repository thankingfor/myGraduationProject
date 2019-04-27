package vip.bzsy.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import vip.bzsy.common.CommonResponse;
import vip.bzsy.common.CommonStatus;
import vip.bzsy.common.CommonUtils;
import vip.bzsy.model.Classroom;
import vip.bzsy.service.ClassroomService;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lyf
 * @since 2019-03-23
 */
@Slf4j
@RestController
@RequestMapping(value = "/api" ,produces = "application/json;charset=UTF-8")
public class ClassroomController {

    @RequestMapping(value = "/getClassroom",method = RequestMethod.GET)
    public CommonResponse getClassroom(@RequestParam(value = "id",defaultValue = "-1") Integer classroomId){
        List<Classroom> list = new ArrayList<>();
        if (classroomId==-1){
            list = classroomService.list();
            return CommonResponse.success(list);
        }
        Classroom classroom1 = new Classroom().setId(classroomId).selectById();
        list.add(classroom1);
        return CommonResponse.success(list);
    }

    @RequestMapping(value = "/addClassroom",method = RequestMethod.POST)
    public CommonResponse addClassroom(@RequestBody Classroom classroom){
        Classroom location = classroom.selectOne(new QueryWrapper<Classroom>().eq("location", classroom.getLocation()));
        if (CommonUtils.isNotEmpty(location)){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        classroom.insert();
        return CommonResponse.success();
    }

    @Autowired
    private ClassroomService classroomService;


}

