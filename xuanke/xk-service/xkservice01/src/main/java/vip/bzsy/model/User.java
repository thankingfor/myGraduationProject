package vip.bzsy.model;

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
 * @since 2019-03-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User extends Model<User> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String account;

    private String password;

    private String name;

    /**
     * 三种身份'student','teacher','admin'
     */
    private String identity;

    /**
     * 0不删除 1删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 院系
     */
    private Integer faculityId;

    @TableField(exist = false)
    private String faculty;

    /**
     * ru xue
     */
    @TableField("enterTime")
    private Long enterTime;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
