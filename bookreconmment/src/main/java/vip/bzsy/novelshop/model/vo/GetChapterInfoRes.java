package vip.bzsy.novelshop.model.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author lyf
 * @create 2019-03-14 20:35
 */
@Data
@ToString
public class GetChapterInfoRes {

    private Integer id;
    private Integer novelId;
    private Integer chapter;
    private String title;
    private String content;
}
