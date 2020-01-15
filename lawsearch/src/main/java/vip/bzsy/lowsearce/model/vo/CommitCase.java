package vip.bzsy.lowsearce.model.vo;


import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CommitCase {

    private Integer uid;
    private String account;
    private String title;
    private String name;
    private Date commitTime;
    private String content;
    private String section;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public class sort{
        private String brief;
        private String caseLevel;
        private String court;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public class tag{
        private String procedure;
        private String courtLevel;
        private String instrument;
        private Date closingTime;
        private String topic;
    }

}
