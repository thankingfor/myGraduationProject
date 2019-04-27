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
import lombok.ToString;
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
@ToString
public class User extends Model<User> {

    /**
     * 普通用户登录为user  管理员、员工登录为staff
     */
    @TableField(exist = false)
    private String loginWay;

    /**
     * 0 正常 1删除
     */
    @TableLogic
    private Integer deleted;


    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 账号
     */
    private String account;

    /**
     * 密码
     */
    private String password;

    /**
     * 用户名
     */
    private String name;

    /**
     * 普通用户user 会员vip 管理员admin 员工staff
     */
    private String identity;

    /**
     * 资金 
     */
    private BigDecimal money;


    /**
     * 添加事件 时间戳
     */
    private Long created;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
