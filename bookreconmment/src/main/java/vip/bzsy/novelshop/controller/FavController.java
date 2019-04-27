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

    @RequestMapping(value = "/postHistory",method = RequestMethod.POST)
    public CommonResponse postHistory(@RequestBody Map<String,String> maps,HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        Integer userId = (Integer) claims.get("userId");
        favService.postHistory(maps.get("sort"),userId);
        return CommonResponse.success();
    }

    @RequestMapping(value = "/deleteCollectionByNovId" , method = RequestMethod.GET)
    public CommonResponse deleteCollection(Integer id, HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        int status = favService.deleteCollection(id,claims,0);
        if (status <= 0) {
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    @RequestMapping(value = "/getCollectionStatus" , method = RequestMethod.GET)
    public CommonResponse getCollectionStatus(Integer id, HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        int status = favService.getCollectionStatus(id,claims);
        map.clear();
        map.put("status",status);
        return CommonResponse.success(map);
    }

    @RequestMapping(value = "/getStatus" , method = RequestMethod.GET)
    public CommonResponse getStatus(Integer novelId, HttpServletRequest request){
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
    public CommonResponse collection(Integer id, HttpServletRequest request){
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
