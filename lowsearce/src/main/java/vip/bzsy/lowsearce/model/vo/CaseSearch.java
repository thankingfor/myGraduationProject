package vip.bzsy.lowsearce.model.vo;

import lombok.*;

import java.util.Date;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CaseSearch {
    private Integer id;
    private String title;
    private String brief;
    private String caseLevel;
    private String court;
    private Date commitTime;
}
