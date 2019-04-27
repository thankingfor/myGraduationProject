package vip.bzsy.lowsearce.model.vo;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SearchUserRequest {

    private String searchIdentity;
    private String searchWay;
    private String key;
}
