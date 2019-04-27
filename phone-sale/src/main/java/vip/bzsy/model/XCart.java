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
 * </p>
 *
 * @author lyf
 * @since 2019-04-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class XCart extends Model<XCart> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    @TableLogic
    private Integer deleted;

    private Integer itemId;

    private Integer count;

    private String color;

    private String sort;

    /**
     *  (注意：这里存放的为商品单价)
     */
    private BigDecimal price;

    private Long time;

    private Integer uid;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
