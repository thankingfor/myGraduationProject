package vip.bzsy.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.ToString;

/**
 * @author lyf
 * @create 2019-03-24 18:48
 */
@Data
@ToString
public class GetTeacherCourseFinishedRes {
    private Integer id;//课程id
    private String name;
    private String faculityName;
    private Integer credit;
    private Integer semester;
    private Integer proptype;
}
