package vip.bzsy.service;

import vip.bzsy.model.vo.CartVo;

import java.util.List;

/**
 * @author lyf
 * @create 2019-04-06 14:39
 */
public interface ATotalService {
    void recharge(Integer uid,Double money);

    List<CartVo> getCartList(Integer uid);
}
