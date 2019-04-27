package vip.bzsy.model;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2019-03-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Expenditure extends Model<Expenditure> {

    private static final long serialVersionUID = 1L;

    /**
     * 支出表ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 0不删除 1删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 用户id
     */
    private Integer uid;

    private BigDecimal money;

    /**
     * sort_id 消费分类，存放id
     */
    private Integer sortId;

    /**
     * 添加收入的时候弄的
     */
    @TableField(exist = false)
    private Integer sort;

    /**
     * way 消费方式（1、现金，2、支付宝，3、微信，4、其他）
     */
    private Integer way;

    /**
     * 消费时间，13位时间戳
     */
    private Long time;

    /**
     * remark 备注
     */
    private String remark;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
