package vip.bzsy.model.vo;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @author lyf
 * @create 2019-03-27 15:19
 */
@Data
@ToString
public class IncomeVo {
    private Integer id;
    private BigDecimal money;
    private Long time;
    private String remark;
}
