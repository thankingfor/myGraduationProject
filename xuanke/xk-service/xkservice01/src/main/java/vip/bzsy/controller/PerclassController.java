package vip.bzsy.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import vip.bzsy.common.CommonResponse;
import vip.bzsy.common.CommonStatus;
import vip.bzsy.common.CommonUtils;
import vip.bzsy.model.*;
import vip.bzsy.model.vo.GetChooseCourseListRes;
import vip.bzsy.model.vo.GetTeacherClassRes;
import vip.bzsy.service.*;

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
public class PerclassController {

    @RequestMapping(value = "/getClassByCourse",method = RequestMethod.GET)
    public CommonResponse getChooseCourseList(@RequestParam("id") Integer courseId){
            List<GetTeacherClassRes> list = courseService.getChooseCourseList(courseId);
        return CommonResponse.success(list);
    }

    /**
     *
     * @param faculity
     * @param semester
     * @return
     */
    @RequestMapping(value = "/getChooseCourseList",method = RequestMethod.GET)
    public CommonResponse getChooseCourseList(@RequestParam("faculity") String faculity,
                                              @RequestParam("semester") Integer semester){
        Faculity f = faculityService.getOne(new QueryWrapper<Faculity>().eq("name", faculity));
        if (CommonUtils.isEmpty(f)){
            return CommonResponse.fail("院系不存在！");
        }
        List<GetChooseCourseListRes> resList = new ArrayList<>();
        List<Course> list = courseService.list(new QueryWrapper<Course>()
                .eq("semester", semester).eq("faculity_id", f.getId()).eq("status", 1));
        for (Course course:list){
            GetChooseCourseListRes res = new GetChooseCourseListRes();
            BeanUtils.copyProperties(course,res);
            res.setFaculityName(new Faculity().setId(course.getFaculityId()).selectById().getName());
            res.setTeacher(new User().setId(course.getTeacherId()).selectById().getName());
            resList.add(res);
        }
        return CommonResponse.success(resList);
    }

    @RequestMapping(value = "/getStudentClass",method = RequestMethod.GET)
    public CommonResponse getStudentClass(@RequestParam("id") Integer studentId){
        List<GetTeacherClassRes> resList = perclassService.getStudentClass(studentId);
        return CommonResponse.success(resList);
    }

    @RequestMapping(value = "/addClass",method = RequestMethod.POST)
    public CommonResponse addClass(@RequestBody Perclass perclass){
        //获取空余的教室
        Classroom classroom = classroomService.getOneEmptyClassRoom(perclass);
        if (CommonUtils.isEmpty(classroom)){
            return CommonResponse.fail("没有教室剩余！");
        }
        //如果课程id 和 day time都一样的话  不让添加
        List<Perclass> perclasses = perclass.selectList(new QueryWrapper<Perclass>()
                .eq("course_id", perclass.getCourseId())
                .eq("day", perclass.getDay()).eq("time", perclass.getTime()));
        if (CommonUtils.isNotEmpty(perclasses)){
            return CommonResponse.fail("这门课在这个时间已经添加");
        }
        boolean insert = perclass.setClassroomId(classroom.getId()).insert();
        if (!insert){
           return CommonResponse.fail("插入数据异常");
        }
        return CommonResponse.success();
    }

    @Autowired
    private ClassroomService classroomService;

    @Autowired
    private ChooseService chooseService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private PerclassService perclassService;

    @Autowired
    private FaculityService faculityService;
}

