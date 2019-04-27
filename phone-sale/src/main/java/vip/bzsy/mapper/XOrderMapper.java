package vip.bzsy.mapper;

import vip.bzsy.model.XOrder;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 注意：这里的color、sort、price都是固定的字符串并非数组
当一条商品订单被创建时，它的初始状态为1，快递单号为空；
当管理员填写快递单号后，它的状态会改变为2； Mapper 接口
 * </p>
 *
 * @author lyf
 * @since 2019-04-07
 */
public interface XOrderMapper extends BaseMapper<XOrder> {

}
