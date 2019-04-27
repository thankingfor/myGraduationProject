package vip.bzsy.service.impl;

import vip.bzsy.model.XOrder;
import vip.bzsy.mapper.XOrderMapper;
import vip.bzsy.service.XOrderService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 注意：这里的color、sort、price都是固定的字符串并非数组
当一条商品订单被创建时，它的初始状态为1，快递单号为空；
当管理员填写快递单号后，它的状态会改变为2； 服务实现类
 * </p>
 *
 * @author lyf
 * @since 2019-04-07
 */
@Service
public class XOrderServiceImpl extends ServiceImpl<XOrderMapper, XOrder> implements XOrderService {

}
