package vip.bzsy.mapper;

import vip.bzsy.model.Classroom;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import vip.bzsy.model.Perclass;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lyf
 * @since 2019-03-23
 */
public interface ClassroomMapper extends BaseMapper<Classroom> {

    Classroom getOneEmptyClassRoom(Perclass perclass);
}
