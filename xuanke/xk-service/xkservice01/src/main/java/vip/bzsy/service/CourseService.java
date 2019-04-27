package vip.bzsy.service;

import vip.bzsy.model.Course;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.bzsy.model.vo.GetTeacherClassRes;
import vip.bzsy.model.vo.GetTeacherCourseFinishedRes;
import vip.bzsy.model.vo.StudentGradeRes;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lyf
 * @since 2019-03-23
 */
public interface CourseService extends IService<Course> {

    List<GetTeacherClassRes> getTeacherClass(Integer teacherId);

    List<GetTeacherClassRes> getChooseCourseList(Integer courseId);

    List<StudentGradeRes> getStudentGrade(Integer studentId, String semester);
}
