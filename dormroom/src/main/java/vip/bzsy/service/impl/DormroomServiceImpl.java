package vip.bzsy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import vip.bzsy.model.Dormroom;
import vip.bzsy.mapper.DormroomMapper;
import vip.bzsy.service.DormroomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lyf
 * @since 2019-04-26
 */
@Service
public class DormroomServiceImpl extends ServiceImpl<DormroomMapper, Dormroom> implements DormroomService {

    @Autowired
    private DormroomMapper dormroomMapper;

    @Override
    @Transactional
    public void removeMember(Integer uid, Integer roomId) {
        dormroomMapper.removeMember(roomId);
        dormroomMapper.setRommNullByUid(uid);
    }
}
