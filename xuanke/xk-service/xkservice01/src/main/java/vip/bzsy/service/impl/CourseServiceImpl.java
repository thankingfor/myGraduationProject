package vip.bzsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import vip.bzsy.mapper.PerclassMapper;
import vip.bzsy.model.Classroom;
import vip.bzsy.model.Course;
import vip.bzsy.mapper.CourseMapper;
import vip.bzsy.model.Perclass;
import vip.bzsy.model.User;
import vip.bzsy.model.vo.GetTeacherClassRes;
import vip.bzsy.model.vo.GetTeacherCourseFinishedRes;
import vip.bzsy.model.vo.StudentGradeRes;
import vip.bzsy.service.CourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lyf
 * @since 2019-03-23
 */
@Slf4j
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {

    /**
     * 获取教师排课信息（这些课对应的课程状态为未结课）
     * 老师要存在  课节也存在
     * @param teacherId
     * @return
     */
    @Override
    public List<GetTeacherClassRes> getTeacherClass(Integer teacherId) {
        List<GetTeacherClassRes> resList = courseMapper.getTeacherClass(teacherId);
//        User teacher = new User().setId(teacherId).selectById();
//        List<Course> courseList = new Course().selectList(new QueryWrapper<Course>()
//                .eq("teacher_id", teacherId).eq("status", 1));
//        log.info(courseList.toString());
//        List<GetTeacherClassRes> resList = new ArrayList<>();
//        for (Course course:courseList){
//            List<Perclass> perclassesList = perclassMapper.selectList(new QueryWrapper<Perclass>()
//                    .eq("course_id",course.getId()));
//            log.info(course.getId()+":::"+perclassesList.toString());
//            for (Perclass perclass:perclassesList){
//                GetTeacherClassRes res = new GetTeacherClassRes();
//                res.setId(perclass.getId());
//                res.setCourseId(course.getId());
//                res.setTeacherId(teacherId);
//                res.setDay(perclass.getDay());
//                res.setTime(perclass.getTime());
//                //通过classroomId查询加送hi
//                Classroom classroom = new Classroom().setId(perclass.getClassromId()).selectById();
//                res.setClassroom(classroom.getLocation());
//                res.setCourseName(course.getName());
//                res.setTeacherName(teacher.getName());
//                resList.add(res);
//            }
//        }
        return resList;
    }

    @Override
    public List<GetTeacherClassRes> getChooseCourseList(Integer courseId) {
        return courseMapper.getChooseCourseList(courseId);
    }

    @Override
    public List<StudentGradeRes> getStudentGrade(Integer studentId, String semester) {
        return courseMapper.getStudentGrade(studentId,semester);
    }

    @SuppressWarnings("all")
    @Autowired
    private PerclassMapper perclassMapper;

    @SuppressWarnings("all")
    @Autowired
    private CourseMapper courseMapper;
}
