package vip.bzsy.service.impl;

import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import vip.bzsy.common.CommonUtils;
import vip.bzsy.common.Md5Utils;
import vip.bzsy.mapper.FaculityMapper;
import vip.bzsy.model.Faculity;
import vip.bzsy.model.User;
import vip.bzsy.mapper.UserMapper;
import vip.bzsy.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lyf
 * @since 2019-03-22
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public boolean register(User user) {
        AbstractWrapper wrapper = new QueryWrapper<User>();
        wrapper.eq("account",user.getAccount());
        User user1 = user.selectOne(wrapper);
        if (CommonUtils.isNotEmpty(user1)){
            return false;
        }
        Faculity faculity = faculityMapper.selectOne(new QueryWrapper<Faculity>().eq("name", user.getFaculty()));
        user.setFaculityId(faculity.getId());
        user.setPassword(Md5Utils.encryptPassword(user.getPassword(),SALT,PASS_COUNT));
        return save(user);
    }

    /**
     * 通过时间戳获得第几学期
     * -1代表时间不规范  还未入学
     * 0代表 已经毕业
     * @param enterTime
     * @return
     */
    @Override
    public int getSemester(Long enterTime) {
        Long now = new Date().getTime();
        Long halfYeal = 180*24*60*60*1000L;
        Long c = now-enterTime;
        if (c >0){
            Long semester = c / halfYeal + 1;
            if(semester.intValue() > 8){
                return 0;
            }
            return semester.intValue();
        }
        return -1;
    }

    @SuppressWarnings("all")
    @Autowired
    private FaculityMapper faculityMapper;

    @Value("${SALT}")
    private String SALT;

    @Value("${PASS_COUNT}")
    private Integer PASS_COUNT;
}
