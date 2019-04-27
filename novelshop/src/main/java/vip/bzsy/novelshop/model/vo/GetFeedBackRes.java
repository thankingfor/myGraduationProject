package vip.bzsy.novelshop.model.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author lyf
 * @create 2019-03-16 19:39
 */
@Data
@ToString
public class GetFeedBackRes {
    private Integer id;
    private String content;
    private String email;
}
