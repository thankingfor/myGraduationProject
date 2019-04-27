package vip.bzsy.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author lyf
 * @create 2019-03-28 14:15
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SortTypeVo {
    private Integer id;
    private String sort;
    private Integer status;
}
