package vip.bzsy.novelshop.model.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author lyf
 * @create 2019-03-13 11:34
 */
@Data
@ToString
public class AddChapterReq {
    private Integer id;

    private Integer novelId;

    private Integer chapter;

    private String title;

    private String content;

    private Integer upload;
}
