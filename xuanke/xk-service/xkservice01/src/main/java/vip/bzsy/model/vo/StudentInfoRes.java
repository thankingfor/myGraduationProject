package vip.bzsy.model.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author lyf
 * @create 2019-03-23 14:49
 */
@Data
@ToString
public class StudentInfoRes {
    private Integer id;
    private String account;
    private String name;
    private String faculity;
}
