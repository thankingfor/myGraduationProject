package vip.bzsy.lowsearce.model.vo;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SearchUserResponse {

    private Integer uid;
    private String name;
    private String identity;
    private String account;
    private String email;
    private String phone;
    private Integer loginCount;
    private List<FeedBackVO> feedback;
}
