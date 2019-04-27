package vip.bzsy.lowsearce.mapper;

import org.springframework.stereotype.Repository;
import vip.bzsy.lowsearce.model.Law;
import vip.bzsy.lowsearce.model.vo.GetFeedbackRes;
import vip.bzsy.lowsearce.model.vo.LawSearch;

import java.util.List;

@Repository
public interface LawMapper {
    List<Law> getLaw(Law law);

    List<Law> searchLaw(LawSearch lawSearch);

    List<GetFeedbackRes> getFeedback();
}
