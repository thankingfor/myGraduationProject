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
public class Unit extends Model<Unit> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer wordId;

    private Integer unit;

    @TableLogic
    private Integer deleted;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
