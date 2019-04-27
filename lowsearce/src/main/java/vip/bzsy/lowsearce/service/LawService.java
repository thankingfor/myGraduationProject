package vip.bzsy.lowsearce.service;

import vip.bzsy.lowsearce.common.JsonReturn;
import vip.bzsy.lowsearce.model.Law;
import vip.bzsy.lowsearce.model.vo.CaseSearch;
import vip.bzsy.lowsearce.model.vo.GetCommitCaseRequest;
import vip.bzsy.lowsearce.model.vo.LawSearch;

import java.util.List;
import java.util.Map;

public interface LawService {
    List<Law> getLaw(String part, String sort);

    JsonReturn getSidebar();

    List<Law> searchLaw(LawSearch lawSearch);

    JsonReturn getSidebarInSearch(LawSearch lawSearch);

    JsonReturn getCaseSort(String sort);

    JsonReturn commitCase(Map<String, Object> jsonmap);

    List<CaseSearch> searchCase(Map<String, Object> jsonmap);

    Map<String ,Object> getSidebarInCaseSearch(Map<String, Object> jsonmap);

    JsonReturn getCaseDetail(Integer id);

    List<CaseSearch> getCommitCase(GetCommitCaseRequest caseRequest);

    JsonReturn deleteCase(Integer id);

    JsonReturn alterCase(Map<String, Object> jsonmap);

    JsonReturn getFeedback();
}
