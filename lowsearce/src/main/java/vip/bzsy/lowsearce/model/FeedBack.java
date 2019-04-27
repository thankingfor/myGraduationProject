package vip.bzsy.lowsearce.model;


import lombok.*;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class FeedBack {
    private Integer id;
    private String feedbackMsg;
    private String account;
    private Date create;
    private Date update;
}
