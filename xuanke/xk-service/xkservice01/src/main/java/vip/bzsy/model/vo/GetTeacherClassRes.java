package vip.bzsy.model.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author lyf
 * @create 2019-03-23 17:22
 */
@Data
@ToString
public class GetTeacherClassRes {
    private Integer id;//课节id
    private Integer courseId;
    private Integer classroomId;
    private Integer teacherId;
    /**
     * 1-7周一到周日
     */
    private Integer day;
    /**
     * 1 2 3 4 一日四个时间段可以上课
     */
    private Integer time;
    private String classroom;
    private String courseName;
    private String teacherName;
}
