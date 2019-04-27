package vip.bzsy.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.QueryChainWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import vip.bzsy.common.CommonResponse;
import vip.bzsy.common.CommonStatus;
import vip.bzsy.common.CommonUtils;
import vip.bzsy.model.*;
import vip.bzsy.model.vo.GetTeacherClassRes;
import vip.bzsy.model.vo.GetTeacherCourseFinishedRes;
import vip.bzsy.model.vo.StudentGradeRes;
import vip.bzsy.service.ChooseService;
import vip.bzsy.service.CourseService;
import vip.bzsy.service.PerclassService;

import java.util.*;

import static com.baomidou.mybatisplus.core.toolkit.Wrappers.update;

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
public class CourseController {

    @RequestMapping(value = "/getStudentGrade",method = RequestMethod.GET)
    public CommonResponse getStudentGrade(@RequestParam(value = "id") Integer studentId,
                                          @RequestParam(value = "semester") String semester){
        List<StudentGradeRes> list = courseService.getStudentGrade(studentId,semester);

//        List<Choose> chooseList = new Choose().selectList(new QueryWrapper<Choose>().eq("student_id", studentId));
//        List<Map<String,Object>> list = new ArrayList<>();
//        for (Choose choose:chooseList){
//            Map<String,Object> map = new HashMap<>();
//            Course course = new Course().setId(choose.getCourseId()).selectById();
//            map.put("id",choose.getId());
//            map.put("courseId",course.getId());
//            map.put("courseName",course.getName());
//            map.put("grade",choose.getGrade());
//            map.put("credit",course.getCredit());
//            list.add(map);
//        }
        return CommonResponse.success(list);
    }

    @RequestMapping(value = "/getTeacherCourse",method = RequestMethod.GET)
    public CommonResponse getTeacherCourse(@RequestParam(value = "id") Integer teacherId){
        List<Course> list = courseService.list(new QueryWrapper<Course>()
                .eq("teacher_id", teacherId)
                .eq("status",1));
        for (Course course:list){
            Faculity faculity = new Faculity().setId(course.getFaculityId()).selectById();
            course.setFaculity(faculity.getName());
        }
        return CommonResponse.success(list);
    }

    @RequestMapping(value = "/addCourse",method = RequestMethod.POST)
    public CommonResponse addCourse(@RequestBody Course course){
        course.setCreated(new Date().getTime());
        boolean insert = course.insert();
        if (!insert){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    /**
     * 获取教师排课信息（这些课对应的课程状态为未结课）
     * @param teacherId
     * @return
     */
    @RequestMapping(value = "/getTeacherClass",method = RequestMethod.GET)
    public CommonResponse getTeacherClass(@RequestParam(value = "id") Integer teacherId){
        List<GetTeacherClassRes> list = courseService.getTeacherClass(teacherId);
        return CommonResponse.success(list);
    }

    /**
     * 获取某个老师发布的已结课的课程
     * @param teacherId
     * @return
     */
    @RequestMapping(value = "/getTeacherCourseFinished",method = RequestMethod.GET)
    public CommonResponse getTeacherCourseFinished(@RequestParam(value = "id") Integer teacherId){
        List<Course> list1 = courseService.list(new QueryWrapper<Course>().eq("teacher_id", teacherId).eq("status", 2));
        List<GetTeacherCourseFinishedRes> list = new ArrayList<>();
        for (Course course:list1){
            GetTeacherCourseFinishedRes res = new GetTeacherCourseFinishedRes();
            Faculity faculity = new Faculity().setId(course.getFaculityId()).selectById();
            res.setFaculityName(faculity.getName());
            BeanUtils.copyProperties(course,res);
            list.add(res);
        }
        return CommonResponse.success(list);
    }

    @RequestMapping(value = "/getCourseInfo",method = RequestMethod.GET)
    public CommonResponse getCourseInfo(Course cor){
        Course course = cor.selectById();
        Faculity faculity = new Faculity().setId(course.getFaculityId()).selectById();
        User teacher = new User().setId(course.getTeacherId()).selectById();
        Map<String,Object> map = new HashMap<>();
        map.put("name",course.getName());
        map.put("id",course.getId());
        map.put("faculityName",faculity.getName());
        map.put("faculityId",course.getFaculityId());
        map.put("credit",course.getCredit());
        map.put("semester",course.getSemester());
        map.put("proptype",course.getProptype());
        map.put("teacherName",teacher.getName());
        map.put("teacherId",course.getTeacherId());
        map.put("status",course.getStatus());
        return CommonResponse.success(map);
    }

    @RequestMapping(value = "/getCourseStudentInfo",method = RequestMethod.GET)
    public CommonResponse getCourseStudentInfo(@RequestParam(value = "id") Integer courseId){
        List<Choose> chooseList = chooseService.list(new QueryWrapper<Choose>().eq("course_id", courseId));
        List<Map<String,Object>> list = new ArrayList<>();
        for (Choose choose:chooseList){
            Map<String,Object> map = new HashMap<>();
            User user = new User().setId(choose.getStudentId()).selectById();
            map.put("id",user.getId());
            map.put("name",user.getName());
            map.put("grade",choose.getGrade());
            list.add(map);
        }
        return CommonResponse.success(list);
    }

    @RequestMapping(value = "/setGrade",method = RequestMethod.POST)
    public CommonResponse setGrade(@RequestBody Map<String,Object> map){
        Integer courseId = Integer.valueOf((String) map.get("courseId"));
        Integer studentId = Integer.valueOf((String) map.get("studentId"));
        Choose choose1 = chooseService.getOne(new QueryWrapper<Choose>()
                .eq("course_id", courseId)
                .eq("student_id", studentId));
        if (CommonUtils.isEmpty(choose1)){
            return CommonResponse.fail("数据不存在");
        }
        boolean choose = new Choose().setGrade(Integer.valueOf((String) map.get("grade"))).setId(choose1.getId()).updateById();
        if (!choose){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    @RequestMapping(value = "/finishedCourse",method = RequestMethod.GET)
    public CommonResponse finishedCourse(@RequestParam(value = "id")Integer courseId){
        boolean b = courseService.updateById(new Course().setId(courseId).setStatus(2));
        if (!b){
            return CommonResponse.fail("结课失败");
        }
        perclassService.remove(new QueryWrapper<Perclass>().eq("course_id", courseId));
        return CommonResponse.success();
    }
    @Autowired
    private CourseService courseService;

    @Autowired
    private ChooseService chooseService;

    @Autowired
    private PerclassService perclassService;
}

