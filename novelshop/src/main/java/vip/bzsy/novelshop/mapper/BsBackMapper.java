package vip.bzsy.novelshop.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import vip.bzsy.novelshop.model.BsBack;
import vip.bzsy.novelshop.model.BsBackExample;

public interface BsBackMapper {
    int countByExample(BsBackExample example);

    int deleteByExample(BsBackExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BsBack record);

    int insertSelective(BsBack record);

    List<BsBack> selectByExample(BsBackExample example);

    BsBack selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BsBack record, @Param("example") BsBackExample example);

    int updateByExample(@Param("record") BsBack record, @Param("example") BsBackExample example);

    int updateByPrimaryKeySelective(BsBack record);

    int updateByPrimaryKey(BsBack record);
}