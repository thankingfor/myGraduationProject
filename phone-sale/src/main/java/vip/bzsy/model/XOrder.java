package vip.bzsy.model;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 注意：这里的color、sort、price都是固定的字符串并非数组
当一条商品订单被创建时，它的初始状态为1，快递单号为空；
当管理员填写快递单号后，它的状态会改变为2；
 * </p>
 *
 * @author lyf
 * @since 2019-04-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class XOrder extends Model<XOrder> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableLogic
    private Integer deleted;

    private Integer uid;

    /**
     * 商品id
     */
    private Integer itemId;

    /**
     *  购买数量
     */
    private Integer count;

    /**
     * 所选商品颜色
     */
    private String color;

    /**
     * 所选商品规格
     */
    private String sort;

    /**
     * 价格（不能从商品表中获取，价格是根据所选商品的规格而改变的）
     */
    private BigDecimal price;

    /**
     * 订单状态 （1未发货，2已发货，3已确认收货，4申请退货，5已退货，6拒绝退货, 7已评价）
     */
    private Integer status;

    /**
     * delivery 快递单号
     */
    private String delivery;

    /**
     * 时间戳
     */
    private Long time;

    private String receive;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
