package vip.bzsy.novelshop.model.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author lyf
 * @create 2019-03-13 14:53
 */
@Data
@ToString
public class GetDraftRes {

    private String title;
    private String content;
    private Integer chapter;
    private Integer novelId;
}
