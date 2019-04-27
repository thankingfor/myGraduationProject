package vip.bzsy.novelshop.model.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author lyf
 * @create 2019-03-16 13:54
 */
@Data
@ToString
public class GetNovelDataRes {
    private Integer collection;
    private Integer download;
    private Integer comment;
    private Integer click;
}
