package vip.bzsy.novelshop.model.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author lyf
 * @create 2019-03-13 12:26
 */
@Data
@ToString
public class AddChapterToDraftReq {

    private String title;
    private String content;
    private Integer novelId;
    private Integer chapter;
}
