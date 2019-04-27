package vip.bzsy.lowsearce.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import vip.bzsy.lowsearce.model.Sys;
import vip.bzsy.lowsearce.model.vo.LawSearch;
import vip.bzsy.lowsearce.model.vo.SideBar;

import java.util.List;
import java.util.Map;

@Repository
public interface SysMapper {

    List<Sys> getListByKey(String key);

    List<SideBar> getSideBar(@Param("key") String key);

    /**
     * 现行有效
     */
    SideBar isNowInvalid();

    /**
     * 尚未生效
     */
    SideBar isNoInvalid();
    /**
     * 已被修改
     */
    SideBar isAlter();
    /**
     * 失效
     */
    SideBar isInvalid();
    /**
     * 部分失效
     */
    SideBar isInvalidPart();

    List<SideBar> getSidebarInSearch(@Param("keys") String key,@Param("law") LawSearch lawSearch);

    SideBar getSidebarInSearchTime(@Param("time") int i,@Param("law") LawSearch lawSearch);

    List<SideBar> getSidebarInCaseSearch(@Param("keys") String key, @Param("map") Map<String, Object> jsonmap);
}
