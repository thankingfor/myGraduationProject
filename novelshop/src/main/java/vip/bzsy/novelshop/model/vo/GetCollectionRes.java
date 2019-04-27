package vip.bzsy.novelshop.model.vo;

import lombok.Data;
import lombok.ToString;

/**
 * @author lyf
 * @create 2019-03-13 19:00
 */
@Data
@ToString
public class GetCollectionRes {

    private Integer id;
    private Integer novelId;
    private String cover;
    private String novelName;
    private String introduction;
}
