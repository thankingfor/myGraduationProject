package vip.bzsy.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import vip.bzsy.model.Expenditure;
import vip.bzsy.mapper.ExpenditureMapper;
import vip.bzsy.service.ExpenditureService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lyf
 * @since 2019-03-27
 */
@Service
public class ExpenditureServiceImpl extends ServiceImpl<ExpenditureMapper, Expenditure> implements ExpenditureService {

    @Override
    public BigDecimal getTotalCast(Integer userId) {
        return expenditureMapper.getTotalCast(userId);
    }

    @Override
    public List<BigDecimal> getTotalCastList() {
        return expenditureMapper.getTotalCastList();
    }

    @Override
    public Map<String,Object> mostWay(Integer userId) {
        return expenditureMapper.mostWay(userId);
    }

    @Override
    public List<Integer> getUidList() {
        return expenditureMapper.getUidList();
    }

    @Autowired
    private ExpenditureMapper expenditureMapper;
}
