package vip.bzsy.model;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * // sort示例
[
    {
        sort: "6G+128G",
        price: "1999"
    },
    {
        sort: "8G+128G",
        price: "2399"
    }
]
 * </p>
 *
 * @author lyf
 * @since 2019-04-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class XItem extends Model<XItem> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableLogic
    private Integer deleted;

    private String title;

    /**
     * (商品的初始价格，会随着选择套餐的改变而改变)
     */
    private BigDecimal price;

    /**
     * 销售量
     */
    @TableField("saleNum")
    private Integer saleNum;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 商品介绍（类型为text）
     */
    private String introduction;

    /**
     * 可选颜色（一个数组，如：['蓝色', '红色', '黑色']）
     */
    private String color;

    /**
     * 可选规格（一个数组，包含若干个对象，示例如下）
     */
    private String sort;

    /**
     * 商品状态（1状态正常，0已下架）
     */
    private Integer status;

    /**
     * 图片url
     */
    private String cover;

    private Long time;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
