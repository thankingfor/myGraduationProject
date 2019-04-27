package vip.bzsy.lowsearce.model;


import lombok.*;

import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Case {

    private Integer id;
    private String account;
    private String title;
    private String name;
    private String content;
    private String brief;
    private String caseLevel;
    private String court;
    private String procedure;
    private String courtLevel;
    private String instrument;
    private Date closingTime;
    private Date commitTime;
    private Date update;
    private String section;
    private String topic;

}
