package vip.bzsy.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import vip.bzsy.mapper.CourseMapper;
import vip.bzsy.model.Perclass;
import vip.bzsy.mapper.PerclassMapper;
import vip.bzsy.model.vo.GetTeacherClassRes;
import vip.bzsy.service.PerclassService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lyf
 * @since 2019-03-23
 */
@Slf4j
@Service
public class PerclassServiceImpl extends ServiceImpl<PerclassMapper, Perclass> implements PerclassService {

    /**
     * 获取某个学生的课节（这些课对应的课程状态为未结课）
     * @param studentId
     * @return
     */
    @Override
    public List<GetTeacherClassRes> getStudentClass(Integer studentId) {
        return courseMapper.getStudentClass(studentId);
    }

    @Autowired
    private CourseMapper courseMapper;

}
