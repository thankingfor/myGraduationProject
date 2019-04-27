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
 * 
 * </p>
 *
 * @author lyf
 * @since 2019-04-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class XUser extends Model<XUser> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 0未删除  1删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * admin管理员、user普通用户
     */
    private String identity;

    private String account;

    private String password;

    /**
     * 收货地址
     */
    private String receive;

    /**
     * 账户余额
     */
    private BigDecimal money;

    /**
     * 用户名
     */
    private String name;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
