package vip.bzsy.model;

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
 * 
 * </p>
 *
 * @author lyf
 * @since 2019-05-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Okornot extends Model<Okornot> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer uid;

    /**
     * 默认0未审核 审核为1
     */
    private Integer okornot;

    @TableLogic
    private Integer deleted;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
