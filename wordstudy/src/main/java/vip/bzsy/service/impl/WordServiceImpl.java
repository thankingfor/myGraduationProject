package vip.bzsy.service.impl;

import vip.bzsy.model.Word;
import vip.bzsy.mapper.WordMapper;
import vip.bzsy.service.WordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lyf
 * @since 2019-04-24
 */
@Service
public class WordServiceImpl extends ServiceImpl<WordMapper, Word> implements WordService {

}
