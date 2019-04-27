package vip.bzsy.novelshop.model.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author lyf
 * @create 2019-03-16 13:18
 */
@Data
@ToString
public class GetUserListRes {
    private Integer id;
    private String identity;
    private String name;
    private String account;
    private Integer vp;
    private Integer ban;
    private String[] like;
}
