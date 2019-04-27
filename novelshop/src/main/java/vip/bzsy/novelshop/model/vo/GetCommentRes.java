package vip.bzsy.novelshop.model.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author lyf
 * @create 2019-03-14 15:13
 */
@Data
@ToString
public class GetCommentRes {
    private Integer id;
    private Integer uid;
    private String name;
    private String content;
    private String time;
}
