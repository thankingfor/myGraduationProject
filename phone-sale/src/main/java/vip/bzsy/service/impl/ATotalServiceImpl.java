package vip.bzsy.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.bzsy.mapper.ATotalMapper;
import vip.bzsy.model.vo.CartVo;
import vip.bzsy.service.ATotalService;

import java.util.List;

/**
 * @author lyf
 * @create 2019-04-06 14:39
 */
@Service
@Slf4j
public class ATotalServiceImpl implements ATotalService {
    @SuppressWarnings("all")
    @Autowired
    private ATotalMapper totalMapper;
    @Override
    public void recharge(Integer uid,Double money) {
        totalMapper.recharge(uid,money);
    }

    @Override
    public List<CartVo> getCartList(Integer uid) {
        return totalMapper.getCartList(uid);
    }
}
