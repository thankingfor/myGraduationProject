package vip.bzsy.model.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author lyf
 * @create 2019-03-25 13:31
 */
@Data
@ToString
public class StudentGradeRes {
    private Integer id;
    private Integer courseId;
    private String courseName;
    private String grade="";
    private Integer credit;
}
