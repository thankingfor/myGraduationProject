package vip.bzsy.service;

import vip.bzsy.model.Perclass;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.bzsy.model.vo.GetTeacherClassRes;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lyf
 * @since 2019-03-23
 */
public interface PerclassService extends IService<Perclass> {

    List<GetTeacherClassRes> getStudentClass(Integer studentId);
}
