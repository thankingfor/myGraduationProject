package vip.bzsy.novelshop.model.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author lyf
 * @create 2019-03-13 15:16
 */
@Data
@ToString
public class GetNewPublishRes {
    private Integer id;
    private String cover;
    private String novelName;
    private String introduction;
}
