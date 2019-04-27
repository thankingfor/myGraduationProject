package vip.bzsy.model.vo;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @author lyf
 * @create 2019-04-06 16:02
 */
@Data
@ToString
public class CartVo {

    private Integer id;

    private Integer itemId;

    private String title;

    private String cover;

    private Integer count;

    private String color;

    private String sort;

    private BigDecimal price;
}
