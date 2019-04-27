package vip.bzsy.mapper;

import vip.bzsy.model.Expenditure;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lyf
 * @since 2019-03-27
 */
public interface ExpenditureMapper extends BaseMapper<Expenditure> {

    BigDecimal getTotalCast(Integer userId);

    List<BigDecimal> getTotalCastList();

    Map<String,Object> mostWay(Integer userId);

    List<Integer> getUidList();
}
