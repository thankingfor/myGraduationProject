package vip.bzsy.lowsearce.model.vo;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GetCommitCaseRequest {

    private Integer page = 1;
    private String section;
    private Integer userId;
    private String account;
}
