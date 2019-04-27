package vip.bzsy.mapper;

import org.apache.ibatis.annotations.Param;
import vip.bzsy.model.vo.CartVo;

import java.util.List;

/**
 * @author lyf
 * @create 2019-04-06 14:41
 */
public interface ATotalMapper {
    void recharge(@Param("uid") Integer uid,@Param("money") Double money);

    List<CartVo> getCartList(Integer uid);
}
