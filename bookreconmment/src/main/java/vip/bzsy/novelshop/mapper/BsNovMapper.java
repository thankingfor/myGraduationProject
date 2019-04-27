package vip.bzsy.novelshop.mapper;

import org.apache.ibatis.annotations.Param;
import vip.bzsy.novelshop.model.BsNov;
import vip.bzsy.novelshop.model.BsNovExample;
import vip.bzsy.novelshop.model.vo.GetNewPublishRes;

import java.util.List;

public interface BsNovMapper {
    int countByExample(BsNovExample example);

    int deleteByExample(BsNovExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BsNov record);

    int insertSelective(BsNov record);

    List<BsNov> selectByExample(BsNovExample example);

    BsNov selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BsNov record, @Param("example") BsNovExample example);

    int updateByExample(@Param("record") BsNov record, @Param("example") BsNovExample example);

    int updateByPrimaryKeySelective(BsNov record);

    int updateByPrimaryKey(BsNov record);

    List<BsNov> selectBySortRun(@Param("sort") String sort, @Param("size") int size);

    List<GetNewPublishRes> getNewPublish();
}