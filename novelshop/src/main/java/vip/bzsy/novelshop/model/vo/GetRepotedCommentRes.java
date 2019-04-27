package vip.bzsy.novelshop.model.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author lyf
 * @create 2019-03-16 13:45
 */
@Data
@ToString
public class GetRepotedCommentRes {
    private Integer id;
    private Integer uid;
    private String name;
    private String content;
}
