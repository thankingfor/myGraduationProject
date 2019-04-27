package vip.bzsy.lowsearce.model.vo;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CaseDetail {
    private String title;
    private String content;
    private Date commitTime;
    private String section;
    private String brief;
    private String caseLevel;
    private String court;

    private String courtLevel;
    private String procedure;
    private String instrument;
    private Date closingTime;
    private String topic;
}
