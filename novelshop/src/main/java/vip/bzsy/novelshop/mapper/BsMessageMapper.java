package vip.bzsy.novelshop.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import vip.bzsy.novelshop.model.BsMessage;
import vip.bzsy.novelshop.model.BsMessageExample;
import vip.bzsy.novelshop.model.vo.GetCommentRes;

public interface BsMessageMapper {
    int countByExample(BsMessageExample example);

    int deleteByExample(BsMessageExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BsMessage record);

    int insertSelective(BsMessage record);

    List<BsMessage> selectByExample(BsMessageExample example);

    BsMessage selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BsMessage record, @Param("example") BsMessageExample example);

    int updateByExample(@Param("record") BsMessage record, @Param("example") BsMessageExample example);

    int updateByPrimaryKeySelective(BsMessage record);

    int updateByPrimaryKey(BsMessage record);

    List<GetCommentRes> getComment(Integer novelId);
}