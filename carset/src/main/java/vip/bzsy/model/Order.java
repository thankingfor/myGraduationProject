package vip.bzsy.model;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author lyf
 * @since 2019-03-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ToString
@TableName("`order`")
public class Order extends Model<Order> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 车位
     */
    private Integer spaceName;

    /**
     * 1 2 3 d代表租车时长
     */
    private Integer timeLength;

    /**
     * 结束时间 时间戳
     */
    private Long startTime;

    /**
     * 金额
     */
    private BigDecimal cost;

    /**
     * 订单状态 status 1未生效（未达开始租借时间） 2已生效（到开始租借时间） 3（需要退单） 4（已退单）
     */
    private Integer status;

    /**
     * 时间戳  下单时间
     */
    private Long created;

    /**
     * 0正常 1删除
     */
    @TableLogic
    private Integer deleted;

    private Integer uid;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
