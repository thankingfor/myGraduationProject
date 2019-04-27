package vip.bzsy.lowsearce.mapper;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import vip.bzsy.lowsearce.model.vo.CaseDetail;
import vip.bzsy.lowsearce.model.vo.CaseSearch;
import vip.bzsy.lowsearce.model.vo.GetCommitCaseRequest;

import java.util.List;
import java.util.Map;

@Repository
public interface CaseMapper {
    int commitCase(@Param("map") Map<String, Object> map);

    List<CaseSearch> searchCase(@Param("map") Map<String, Object> jsonmap);

    CaseDetail findById(Integer id);

    List<CaseSearch> getCommitCase(GetCommitCaseRequest caseRequest);

    int deleteCase(Integer id);

    int alterCase(@Param("map") Map<String, Object> jsonmap);
}
