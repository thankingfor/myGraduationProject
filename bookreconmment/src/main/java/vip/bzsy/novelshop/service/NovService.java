package vip.bzsy.novelshop.service;

import io.jsonwebtoken.Claims;
import vip.bzsy.novelshop.model.BsChapter;
import vip.bzsy.novelshop.model.vo.*;

import java.util.List;
import java.util.Map;

/**
 * @author lyf
 * @create 2019-03-12 19:13
 */
public interface NovService {
    int publishNovel(PublishNovelReq req, Claims claims);

    List<GetPublishedNovelRes> getPublishedNovel(Integer userId);

    int deleteNovel(Integer id);

    Integer uploadChapter(UploadChapterReq req) throws Exception;

    List<GetNovelChapterRes> getNovelChapter(Integer id);

    Integer addChapter(AddChapterReq req);

    List<FetDraftBoxRes> getDraftBox(Claims claims);

    Integer addChapterToDraft(AddChapterToDraftReq req);

    GetDraftRes getDraft(Integer id);

    Integer saveChapterToDraft(BsChapter chapter);

    Integer deleteDraft(Integer id);

    List<GetNewPublishRes> getNewPublish();

    List<GetNewPublishRes> getRanking(String sort);

    List<GetNewPublishRes> getRecommend(Claims claims);

    List<GetNewPublishRes> getSearch(String searchWay, String key);

    GetNovelInfoRes getNovelInfo(Integer id);

    Integer buyNovel(Integer novId, Claims claims);


    BsChapter download(Integer cId);

    GetChapterInfoRes getChapterInfo(Integer novelId, Integer chapter);

    List<GetCollectionRes> getBought(Claims claims);

    GetNovelDataRes getNovelData(Integer novId);

    List<GetNewPublishRes> getRecommendByHistory(Claims claims);

    int modifyNovel(Map<String, String> maps);
}
