package vip.bzsy.novelshop.service;

import io.jsonwebtoken.Claims;
import vip.bzsy.novelshop.model.BsBack;
import vip.bzsy.novelshop.model.vo.*;

import java.util.List;

/**
 * @author lyf
 * @create 2019-03-13 18:54
 */
public interface FavService {
    List<GetCollectionRes> getCollection(Claims claims);

    int collection(Claims claims,Integer novId);

    int deleteCollection(Integer id);

    int getStatus(Integer novelId, Claims claims);

    int getCollectionStatus(Integer id, Claims claims);

    int deleteCollection(Integer novId, Claims claims,Integer status);

    int publishComment(PublishCommentReq req, Claims claims);

    List<GetCommentRes> getComment(Integer novelId);

    Integer customerService(BsBack bsBack, Claims claims);

    Integer deleteComment(Integer id);

    Integer reportComment(Integer id);

    List<GetRepotedCommentRes> getRepotedComment();

    List<GetFeedBackRes> getFeedback();
}
