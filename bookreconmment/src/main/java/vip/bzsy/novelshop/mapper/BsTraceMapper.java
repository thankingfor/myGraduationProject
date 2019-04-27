package vip.bzsy.novelshop.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import vip.bzsy.novelshop.model.BsTrace;
import vip.bzsy.novelshop.model.BsTraceExample;

public interface BsTraceMapper {
    int countByExample(BsTraceExample example);

    int deleteByExample(BsTraceExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BsTrace record);

    int insertSelective(BsTrace record);

    List<BsTrace> selectByExample(BsTraceExample example);

    BsTrace selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BsTrace record, @Param("example") BsTraceExample example);

    int updateByExample(@Param("record") BsTrace record, @Param("example") BsTraceExample example);

    int updateByPrimaryKeySelective(BsTrace record);

    int updateByPrimaryKey(BsTrace record);
}