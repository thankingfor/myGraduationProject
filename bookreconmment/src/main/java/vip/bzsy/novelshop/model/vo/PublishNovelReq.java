package vip.bzsy.novelshop.model.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author lyf
 * @create 2019-03-12 19:10
 */
@Data
@ToString
public class PublishNovelReq {

    private String novelName;

    private String introduction;

    private String sort;

    private Integer price;

    private String cover;

    private String time;
}
