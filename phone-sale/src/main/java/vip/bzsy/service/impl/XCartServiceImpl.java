package vip.bzsy.service.impl;

import vip.bzsy.model.XCart;
import vip.bzsy.mapper.XCartMapper;
import vip.bzsy.service.XCartService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 注意：这里的color、sort、price都是固定的字符串并非数组 服务实现类
 * </p>
 *
 * @author lyf
 * @since 2019-04-06
 */
@Service
public class XCartServiceImpl extends ServiceImpl<XCartMapper, XCart> implements XCartService {

}
