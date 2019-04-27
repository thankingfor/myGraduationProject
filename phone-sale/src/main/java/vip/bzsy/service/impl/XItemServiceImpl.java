package vip.bzsy.service.impl;

import vip.bzsy.model.XItem;
import vip.bzsy.mapper.XItemMapper;
import vip.bzsy.service.XItemService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * // sort示例
[
    {
        sort: "6G+128G",
        price: "1999"
    },
    {
        sort: "8G+128G",
        price: "2399"
    }
] 服务实现类
 * </p>
 *
 * @author lyf
 * @since 2019-04-06
 */
@Service
public class XItemServiceImpl extends ServiceImpl<XItemMapper, XItem> implements XItemService {

}
