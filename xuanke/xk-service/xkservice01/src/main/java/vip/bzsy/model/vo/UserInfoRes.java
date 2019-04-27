package vip.bzsy.model.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author lyf
 * @create 2019-03-22 21:36
 */
@Data
@ToString
public class UserInfoRes{
    private Integer uid;
    private String account;
    private String name;
    private String identity;
    private String faculity;
    private Integer semester;
}
