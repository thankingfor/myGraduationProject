package vip.bzsy.service;

import vip.bzsy.model.Expenditure;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lyf
 * @since 2019-03-27
 */
public interface ExpenditureService extends IService<Expenditure> {

    BigDecimal getTotalCast(Integer userId);

    List<BigDecimal> getTotalCastList();

    Map<String,Object> mostWay(Integer userId);

    List<Integer> getUidList();
}
