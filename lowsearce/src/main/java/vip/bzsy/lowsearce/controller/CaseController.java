package vip.bzsy.lowsearce.controller;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vip.bzsy.lowsearce.common.Audience;
import vip.bzsy.lowsearce.common.CommonStatus;
import vip.bzsy.lowsearce.common.JsonReturn;
import vip.bzsy.lowsearce.model.User;
import vip.bzsy.lowsearce.model.vo.CaseSearch;
import vip.bzsy.lowsearce.model.vo.GetCommitCaseRequest;
import vip.bzsy.lowsearce.service.LawService;
import vip.bzsy.lowsearce.util.BzsyUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/law",produces = "application/json;charset=UTF-8")
public class CaseController {

    @RequestMapping(value = "/getFeedback" , method = RequestMethod.GET)
    public JsonReturn getFeedback(){
        return lawserviceImpl.getFeedback();
    }

    @RequestMapping(value = "/alterCase" , method = RequestMethod.POST)
    public JsonReturn alterCase(@RequestBody Map<String ,Object> jsonmap){
        return lawserviceImpl.alterCase(jsonmap);
    }

    @RequestMapping(value = "/deleteCase" , method = RequestMethod.GET)
    public JsonReturn deleteCase(Integer id , HttpServletRequest request){
        return lawserviceImpl.deleteCase(id);
    }

    @RequestMapping(value = "/getCommitCase" , method = RequestMethod.POST)
    public JsonReturn getCommitCase(@RequestBody GetCommitCaseRequest caseRequest , HttpServletRequest request){
        Claims claims = (Claims)request.getAttribute("CLAIMS");
        if (BzsyUtils.isEmpty(claims)) return JsonReturn.fail(CommonStatus.FAULT);
        caseRequest.setUserId((Integer) claims.get("userid"));
        caseRequest.setAccount((String) claims.get("account"));
        PageHelper.startPage(caseRequest.getPage(),15);
        List<CaseSearch> list = lawserviceImpl.getCommitCase(caseRequest);
        PageInfo<CaseSearch> pageInfo = new PageInfo<>(list);
        map.clear();
        map.put("total",pageInfo.getTotal());
        map.put("article",list);
        return JsonReturn.success(map);
    }

    @RequestMapping(value = "/getCaseDetail" , method = RequestMethod.GET)
    public JsonReturn getCaseDetail(@RequestParam Integer id){
        return lawserviceImpl.getCaseDetail(id);
    }

    @RequestMapping(value = "/searchCase" , method = RequestMethod.POST)
    public JsonReturn searchCase(@RequestBody Map<String ,Object> jsonmap){
        log.info("json="+jsonmap);
        if (jsonmap.get("page") == null){
            return JsonReturn.fail(CommonStatus.FAULT);
        }
        PageHelper.startPage(Integer.valueOf((String) jsonmap.get("page")) ,15);
        List<CaseSearch> list = lawserviceImpl.searchCase(jsonmap);
        PageInfo<CaseSearch> pageInfo = new PageInfo<>(list);
        Map<String ,Object> mapSide = lawserviceImpl.getSidebarInCaseSearch(jsonmap);
        map.clear();
        map.put("article",list);
        map.put("total",pageInfo.getTotal());
        map.put("side",mapSide);
        return JsonReturn.success(map);
    }

    @RequestMapping(value = "/commitCase" , method = RequestMethod.POST)
    public JsonReturn commitCase(@RequestBody Map<String ,Object> jsonmap){
        return lawserviceImpl.commitCase(jsonmap);
    }

    @RequestMapping(value = "/getCaseSort" , method = RequestMethod.GET)
    public JsonReturn getCaseSort(String sort){
        return lawserviceImpl.getCaseSort(sort);
    }

    @Autowired
    private LawService lawserviceImpl;

    @Autowired
    private Audience audience;

    private Map<String,Object> map = new HashMap<String,Object>();
}
