package vip.bzsy.mapper;

import org.apache.ibatis.annotations.Param;
import vip.bzsy.model.Dormroom;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lyf
 * @since 2019-04-26
 */
public interface DormroomMapper extends BaseMapper<Dormroom> {

    void setRommNullByUid(@Param("uid") Integer uid);

    void removeMember(@Param("roomId") Integer roomId);
}
