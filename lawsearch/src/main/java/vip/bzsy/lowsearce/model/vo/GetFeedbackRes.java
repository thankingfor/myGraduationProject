package vip.bzsy.lowsearce.model.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author lyf
 * @create 2019-03-18 16:30
 */
@Data
@ToString
public class GetFeedbackRes {

    private Integer id;
    private String content;
    private String account;
}
