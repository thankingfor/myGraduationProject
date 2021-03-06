package vip.bzsy.service;

import vip.bzsy.model.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lyf
 * @since 2019-03-22
 */
public interface UserService extends IService<User> {

    boolean register(User user);

    int getSemester(Long enterTime);
}
