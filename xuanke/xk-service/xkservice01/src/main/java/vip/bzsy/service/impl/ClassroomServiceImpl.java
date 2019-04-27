package vip.bzsy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import vip.bzsy.model.Classroom;
import vip.bzsy.mapper.ClassroomMapper;
import vip.bzsy.model.Perclass;
import vip.bzsy.service.ClassroomService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lyf
 * @since 2019-03-23
 */
@Service
public class ClassroomServiceImpl extends ServiceImpl<ClassroomMapper, Classroom> implements ClassroomService {

    /**
     * 获取一个空教室
     * 查询出当天day  time时间内的所有用的教室
     * 返回没有用的教室
     * 字符串location的名字升序
     * @return
     */
    @Override
    public Classroom getOneEmptyClassRoom(Perclass perclass) {
        Classroom oneEmptyClassRoom = classroomMapper.getOneEmptyClassRoom(perclass);
        return oneEmptyClassRoom;
    }

    @Autowired
    private ClassroomMapper classroomMapper;
}
