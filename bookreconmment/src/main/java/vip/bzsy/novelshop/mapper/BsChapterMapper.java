package vip.bzsy.novelshop.mapper;

import org.apache.ibatis.annotations.Param;
import vip.bzsy.novelshop.model.BsChapter;
import vip.bzsy.novelshop.model.BsChapterExample;

import java.util.List;

public interface BsChapterMapper {
    int countByExample(BsChapterExample example);

    int deleteByExample(BsChapterExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BsChapter record);

    int insertSelective(BsChapter record);

    List<BsChapter> selectByExampleWithBLOBs(BsChapterExample example);

    List<BsChapter> selectByExample(BsChapterExample example);

    BsChapter selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BsChapter record, @Param("example") BsChapterExample example);

    int updateByExampleWithBLOBs(@Param("record") BsChapter record, @Param("example") BsChapterExample example);

    int updateByExample(@Param("record") BsChapter record, @Param("example") BsChapterExample example);

    int updateByPrimaryKeySelective(BsChapter record);

    int updateByPrimaryKeyWithBLOBs(BsChapter record);

    int updateByPrimaryKey(BsChapter record);
}