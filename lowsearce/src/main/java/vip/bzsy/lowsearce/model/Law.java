package vip.bzsy.lowsearce.model;

import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Law {

    private Integer id;
    private Integer userId;
    private String title;
    //发文字号
    private String number;
    //内容
    private String content;
    //发布时间
    private Date commitTime;
    //实施时间
    private Date carryTime;
    //level
    private String level;
    //发布部门
    private String department;
    //法规类别
    private String lowSort;
    //筛选条件
    private String filter;
    //是否部分失效 1是/0否
    private Integer isInvalidPart;
    //是否被修改
    private Integer isAlter;
    //是否失效
    private Integer isInvalid;
    //现行有效
    private Integer isNowInvalid;
    //尚未生效
    private Integer isNoInvalid;
}
