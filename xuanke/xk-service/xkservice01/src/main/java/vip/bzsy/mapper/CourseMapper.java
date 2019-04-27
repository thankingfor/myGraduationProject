package vip.bzsy.mapper;

import org.apache.ibatis.annotations.Param;
import vip.bzsy.model.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import vip.bzsy.model.vo.GetTeacherClassRes;
import vip.bzsy.model.vo.StudentGradeRes;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lyf
 * @since 2019-03-23
 */
public interface CourseMapper extends BaseMapper<Course> {

    List<GetTeacherClassRes> getTeacherClass(Integer teacherId);

    List<GetTeacherClassRes> getStudentClass(Integer studentId);

    List<GetTeacherClassRes> getChooseCourseList(Integer courseId);

    List<StudentGradeRes> getStudentGrade(@Param("studentId") Integer studentId, @Param("semester") String semester);
}
