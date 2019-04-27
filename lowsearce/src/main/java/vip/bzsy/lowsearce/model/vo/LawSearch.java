package vip.bzsy.lowsearce.model.vo;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LawSearch {
    private Integer page = 1;
    private Integer rows = 15;
    //level
    private String level;
    //发布部门
    private String department;
    //法规类别
    private String lowSort;
    //筛选条件
    private String[] filter;
    //搜索类型 搜索类型，1是由标题搜索，2是由标题及内容搜索
    private Integer search;
    //关键词
    private String key;
    //时效性
    private Integer time;
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
