package vip.bzsy.model.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author lyf
 * @create 2019-03-25 12:46
 */
@Data
@ToString
public class GetChooseCourseListRes {

    private Integer id;
    private String name;
    private Integer credit;
    private String faculityName;
    private Integer semester;
    private Integer proptype;
    private String teacher;

}
