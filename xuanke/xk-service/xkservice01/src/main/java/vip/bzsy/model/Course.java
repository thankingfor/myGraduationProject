package vip.bzsy.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * @since 2019-03-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ToString
public class Course extends Model<Course> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private Integer faculityId;

    @TableField(exist = false)
    private String faculity;

    /**
     * 学分
     */
    private Integer credit;

    /**
     * 时间戳  插入时间
     */
    private Long created;

    /**
     * 学期 1-8
     */
    private Integer semester;

    /**
     * 课程属性 1-必修 0-选修
     */
    private Integer proptype;

    private Integer teacherId;

    /**
     * 数据库默认值为1
     * 1结课  2未结课
     */
    private Integer status;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
