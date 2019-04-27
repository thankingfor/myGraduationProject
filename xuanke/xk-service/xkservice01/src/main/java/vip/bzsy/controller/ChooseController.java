package vip.bzsy.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import vip.bzsy.common.CommonResponse;
import vip.bzsy.common.CommonStatus;
import vip.bzsy.model.Choose;
import vip.bzsy.model.vo.GetTeacherClassRes;

import java.util.List;
import java.util.Map;

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
public class ChooseController {

    @RequestMapping(value = "/chooseCourse",method = RequestMethod.POST)
    public CommonResponse chooseCourse(@RequestBody Map<String,Object> map){
        boolean insert = new Choose().setStudentId(
                Integer.valueOf((String) map.get("uid"))
        ).setCourseId(
                Integer.valueOf((String) map.get("courseId"))
        ).insert();
        if (!insert){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

}

