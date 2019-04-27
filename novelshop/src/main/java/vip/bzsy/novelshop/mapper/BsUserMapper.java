package vip.bzsy.novelshop.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import vip.bzsy.novelshop.model.BsUser;
import vip.bzsy.novelshop.model.BsUserExample;

public interface BsUserMapper {
    int countByExample(BsUserExample example);

    int deleteByExample(BsUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BsUser record);

    int insertSelective(BsUser record);

    List<BsUser> selectByExample(BsUserExample example);

    BsUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BsUser record, @Param("example") BsUserExample example);

    int updateByExample(@Param("record") BsUser record, @Param("example") BsUserExample example);

    int updateByPrimaryKeySelective(BsUser record);

    int updateByPrimaryKey(BsUser record);
}