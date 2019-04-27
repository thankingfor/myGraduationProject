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
 * @since 2019-03-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Parking extends Model<Parking> {

    @TableField(exist = false)
    private Integer count;

    /**
     * 0正常 1停用
     */
    @TableLogic
    private Integer deleted;

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 停车场名
     */
    private String parkingName;

    /**
     * 说明
     */
    private String description;

    /**
     * 停车场每小时租金
     */
    private BigDecimal cost;

    /**
     * 停车场图片 
     */
    private String url;



    /**
     * 创建时间  时间戳
     */
    private Long created;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
