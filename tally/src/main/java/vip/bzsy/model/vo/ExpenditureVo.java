package vip.bzsy.model.vo;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @author lyf
 * @create 2019-03-27 13:31
 */
@Data
@ToString
public class ExpenditureVo {
    private Integer id;
    private BigDecimal money;
    private Integer sortId;
    private String sort;
    private Integer way;
    private Long time;
    private String remark;
}
