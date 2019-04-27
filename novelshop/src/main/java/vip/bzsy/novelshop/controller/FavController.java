package vip.bzsy.novelshop.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vip.bzsy.novelshop.common.Audience;
import vip.bzsy.novelshop.common.CommonResponse;
import vip.bzsy.novelshop.common.CommonStatus;
import vip.bzsy.novelshop.model.BsBack;
import vip.bzsy.novelshop.model.vo.*;
import vip.bzsy.novelshop.service.FavService;
import vip.bzsy.novelshop.service.NovService;
import vip.bzsy.novelshop.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lyf
 * @create 2019-03-13 18:53
 */
@RestController
@Slf4j
@RequestMapping(value = "/api/nov" ,produces = "application/json;charset=UTF-8")
public class FavController {

    @RequestMapping(value = "/getFeedback",method = RequestMethod.GET)
    public CommonResponse getFeedback(){
        List<GetFeedBackRes> list = favService.getFeedback();
        return CommonResponse.success(list);
    }

    @RequestMapping(value = "/getRepotedComment" , method = RequestMethod.GET)
    public CommonResponse getRepotedComment(HttpServletRequest request){
        List<GetRepotedCommentRes> list = favService.getRepotedComment();
        return CommonResponse.success(list);
    }

    @RequestMapping(value = "/reportComment" , method = RequestMethod.GET)
    public CommonResponse reportComment(Integer id,HttpServletRequest request){
        Integer status = favService.reportComment(id);
        if (status <= 0){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    @RequestMapping(value = "/deleteComment" , method = RequestMethod.GET)
    public CommonResponse deleteComment(Integer id,HttpServletRequest request){
        Integer status = favService.deleteComment(id);
        if (status <= 0){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    @RequestMapping(value = "/customerService" , method = RequestMethod.POST)
    public CommonResponse customerService(@RequestBody BsBack bsBack,HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        Integer status = favService.customerService(bsBack,claims);
        if (status <= 0){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    @RequestMapping(value = "/getComment" , method = RequestMethod.GET)
    public CommonResponse getComment(@RequestParam("id") Integer novelId,
                                     @RequestParam(value = "page",defaultValue = "1") Integer page,
                                     @RequestParam(value = "size",defaultValue = "5") Integer size,
                                     HttpServletRequest request){
        PageHelper.startPage(page,size);
        List<GetCommentRes> list = favService.getComment(novelId);
        PageInfo<GetCommentRes> pageInfo = new PageInfo<>(list);
        map.clear();
        map.put("comment_data",list);
        map.put("total",pageInfo.getTotal());
        return CommonResponse.success(map);
    }

    @RequestMapping(value = "/publishComment" , method = RequestMethod.POST)
    public CommonResponse publishComment(@RequestBody PublishCommentReq req, HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        int status = favService.publishComment(req,claims);
        if (status <= 0) {
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    @RequestMapping(value = "/deleteCollectionByNovId" , method = RequestMethod.GET)
    public CommonResponse deleteCollection(Integer id,HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        int status = favService.deleteCollection(id,claims,0);
        if (status <= 0) {
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    @RequestMapping(value = "/getCollectionStatus" , method = RequestMethod.GET)
    public CommonResponse getCollectionStatus(Integer id,HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        int status = favService.getCollectionStatus(id,claims);
        map.clear();
        map.put("status",status);
        return CommonResponse.success(map);
    }

    @RequestMapping(value = "/getStatus" , method = RequestMethod.GET)
    public CommonResponse getStatus(Integer novelId,HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        int status = favService.getStatus(novelId,claims);
        map.clear();
        map.put("status",status);
        return CommonResponse.success(map);
    }

    @RequestMapping(value = "/deleteCollection" , method = RequestMethod.GET)
    public CommonResponse deleteCollection(Integer id){
        int status = favService.deleteCollection(id);
        if (status <= 0){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    @RequestMapping(value = "/collection" , method = RequestMethod.GET)
    public CommonResponse collection(Integer id,HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        int status = favService.collection(claims,id);
        if (status <= 0){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    @RequestMapping(value = "/getCollection" , method = RequestMethod.GET)
    public CommonResponse getCollection(HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        List<GetCollectionRes> list = favService.getCollection(claims);
        return CommonResponse.success(list);
    }

    @Autowired
    private FavService favService;

    @Autowired
    private NovService novService;

    @Autowired
    private UserService userService;

    @Autowired
    private Audience audience;

    private Map<String,Object> map = new HashMap<String,Object>();
}
