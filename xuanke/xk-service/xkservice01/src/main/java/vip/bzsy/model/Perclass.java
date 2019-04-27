package vip.bzsy.model;

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
 * @since 2019-03-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Perclass extends Model<Perclass> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer courseId;

    @TableField("classrom_id")
    private Integer classroomId;

    /**
     * 1-7周一到周日
     */
    private Integer day;

    /**
     * 1 2 3 4 一日四个时间段可以上课
     */
    private Integer time;

    /**
     * 0未删除 1删除
     */
    @TableLogic
    private Integer deteled;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
