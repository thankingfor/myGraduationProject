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
 * 
 * </p>
 *
 * @author lyf
 * @since 2019-04-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Repayment extends Model<Repayment> {

    private static final long serialVersionUID = 1L;

    /**
     * 还款倒计时表ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 0不删除 1删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 还款金额
     */
    private BigDecimal money;

    /**
     * 还款标题（如：花呗还款、京东白条还款，存放字符串）
     */
    private String title;

    /**
     * 目标日期，即为还款到期日
     */
    @TableField("targetTime")
    private Long targetTime;

    private Integer uid;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
