package vip.bzsy.novelshop.mapper;

import org.apache.ibatis.annotations.Param;
import vip.bzsy.novelshop.model.BsUserFav;
import vip.bzsy.novelshop.model.BsUserFavExample;

import java.util.List;

public interface BsUserFavMapper {
    int countByExample(BsUserFavExample example);

    int deleteByExample(BsUserFavExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BsUserFav record);

    int insertSelective(BsUserFav record);

    List<BsUserFav> selectByExample(BsUserFavExample example);

    BsUserFav selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BsUserFav record, @Param("example") BsUserFavExample example);

    int updateByExample(@Param("record") BsUserFav record, @Param("example") BsUserFavExample example);

    int updateByPrimaryKeySelective(BsUserFav record);

    int updateByPrimaryKey(BsUserFav record);
}