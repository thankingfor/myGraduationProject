package vip.bzsy.service;

import vip.bzsy.model.Classroom;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.bzsy.model.Perclass;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lyf
 * @since 2019-03-23
 */
public interface ClassroomService extends IService<Classroom> {

    Classroom getOneEmptyClassRoom(Perclass perclass);
}
