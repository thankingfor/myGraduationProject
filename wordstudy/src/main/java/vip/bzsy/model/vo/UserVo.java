package vip.bzsy.model.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author lyf
 * @create 2019-04-24 11:01
 */
@Data
@ToString
public class UserVo {

    private Integer uid;

    private String name;

    private String account;

    private String password;
}
