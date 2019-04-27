package vip.bzsy.lowsearce.model;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

    private Integer id;

    private String account;

    private String password;

    private String email;

    private String phone;

    private String identity;

    private Date create;

    private Date update;

    private String name;

    private Integer loginCount;
}
