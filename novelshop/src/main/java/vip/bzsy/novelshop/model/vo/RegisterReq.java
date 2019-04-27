package vip.bzsy.novelshop.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author lyf
 * @create 2019-03-11 16:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegisterReq {

    private String account;

    private String password;

    private String name;

    private Integer editor;

    private String[] like;
}
