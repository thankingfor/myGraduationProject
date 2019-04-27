package vip.bzsy.lowsearce.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vip.bzsy.lowsearce.common.CommonStatus;
import vip.bzsy.lowsearce.common.JsonReturn;
import vip.bzsy.lowsearce.mapper.CaseMapper;
import vip.bzsy.lowsearce.mapper.LawMapper;
import vip.bzsy.lowsearce.mapper.SysMapper;
import vip.bzsy.lowsearce.model.Law;
import vip.bzsy.lowsearce.model.Sys;
import vip.bzsy.lowsearce.model.vo.*;
import vip.bzsy.lowsearce.service.LawService;
import vip.bzsy.lowsearce.util.BzsyUtils;

import java.util.*;

@Slf4j
@Service
public class LawServiceImpl implements LawService {

    @Override
    public List<Law> getLaw(String part, String sort) {
        Law law = new Law();
        if (part.equals("效力级别")){law.setLevel(sort);}
        if (part.equals("发布部门")){law.setDepartment(sort);}
        if (part.equals("时效性")){
            if(sort.equals("现行有效")){
                law.setIsNowInvalid(1);
            }
            if(sort.equals("失效")){
                law.setIsInvalid(1);
            }
            if(sort.equals("已被修改")){
                law.setIsAlter(1);
            }
            if(sort.equals("尚未生效")){
                law.setIsNoInvalid(1);
            }
            if(sort.equals("部分失效")){
                law.setIsInvalidPart(1);
            }
        }
        if (part.equals("法规类别")){law.setLowSort(sort);}
        log.info("law:::"+law.toString());
        return lawMapper.getLaw(law);
    }

    @Override
    public JsonReturn getSidebar() {
        Map<String,Object> map = new LinkedHashMap<>();
        //先获取level，department，time，lowSort所有数据 除了time 其他都是内链接  只有存在才返回  0 不返回
        List<SideBar> listLevel = sysMapper.getSideBar("level");
        map.put("level",listLevel);
        List<SideBar> listDepartment = sysMapper.getSideBar("department");
        map.put("department",listDepartment);
        //单独获取time中的五个数据
        List<SideBar> listTime = getTime();
        map.put("time",listTime);
        List<SideBar> listLowSort = sysMapper.getSideBar("lowSort");
        map.put("lowSort",listLowSort);
        return JsonReturn.success(map);
    }

    @Override
    public JsonReturn getSidebarInSearch(LawSearch lawSearch) {
        Map<String,Object> map = new LinkedHashMap<>();
        //先获取level，department，time，lowSort所有数据 除了time 其他都是内链接  只有存在才返回  0 不返回
        List<SideBar> listLevel = sysMapper.getSidebarInSearch("level",lawSearch);
        map.put("level",listLevel);
        List<SideBar> listDepartment = sysMapper.getSidebarInSearch("department",lawSearch);
        map.put("department",listDepartment);
        //单独获取time中的五个数据
        List<SideBar> listTime = getTime(lawSearch);
        map.put("time",listTime);
        List<SideBar> listLowSort = sysMapper.getSidebarInSearch("lowSort",lawSearch);
        map.put("lowSort",listLowSort);
        return JsonReturn.success(map);
    }

    @Override
    public JsonReturn getCaseSort(String sort) {
        List<Sys> listByKey = sysMapper.getListByKey(sort);
        String[] value = new String[listByKey.size()];
        for (int i = 0; i < listByKey.size() ;i++){
            value[i] = listByKey.get(i).getValue();
        }
        Map<String , Object> map = new HashMap<>();
        map.put("key",sort);
        map.put("value",value);
        return JsonReturn.success(map);
    }

    @Override
    public JsonReturn commitCase(Map<String, Object> jsonmap) {
        int status = caseMapper.commitCase(jsonmap);
        if (BzsyUtils.isEmpty(status)){
            return JsonReturn.fail(CommonStatus.FAULT);
        }
        return JsonReturn.success();
    }

    @Override
    public List<CaseSearch> searchCase(Map<String, Object> jsonmap) {
        if(jsonmap.get("tag")!=null){
            Map<String , Object> map = (Map<String, Object>) jsonmap.get("tag");
            if(map.get("commitTime")!=null){
                String commitTime = (String) map.get("commitTime");
                if (!commitTime.equals("一月内") && !commitTime.equals("三月内") && !commitTime.equals("半年内") && !commitTime.equals("一年内")){
                    String[] split = commitTime.split("-");
                    jsonmap.put("startCommitTime",split[0]);
                    jsonmap.put("endCommitTime",split[1]);
                }
            }
            if(map.get("closingTime")!=null){
                String closingTime = (String) map.get("closingTime");
                log.info(closingTime);
                if (!closingTime.equals("一月内") && !closingTime.equals("三月内") && !closingTime.equals("半年内") && !closingTime.equals("一年内")){
                    String[] split = closingTime.split("-");
                    jsonmap.put("startClosingTime",split[0]);
                    jsonmap.put("endClosingTime",split[1]);
                }
            }
        }
        log.info("jsonMap="+jsonmap.toString());
        List<CaseSearch> list = caseMapper.searchCase(jsonmap);
        return list;
    }

    @Override
    public Map<String ,Object> getSidebarInCaseSearch(Map<String, Object> jsonmap) {
        Map<String,Object> map = new LinkedHashMap<>();
        //先获取brief，caseLevel，court
        List<SideBar> listLevel = sysMapper.getSidebarInCaseSearch("brief",jsonmap);
        map.put("brief",listLevel);
        List<SideBar> listDepartment = sysMapper.getSidebarInCaseSearch("caseLevel",jsonmap);
        map.put("caseLevel",listDepartment);
        List<SideBar> listLowSort = sysMapper.getSidebarInCaseSearch("court",jsonmap);
        map.put("court",listLowSort);
        return map;
    }

    @Override
    public JsonReturn getCaseDetail(Integer id) {
        CaseDetail caseDetail = caseMapper.findById(id);
        if (BzsyUtils.isEmpty(caseDetail)){
            return JsonReturn.fail(CommonStatus.FAULT);
        }
        return JsonReturn.success(caseDetail);
    }

    @Override
    public List<CaseSearch> getCommitCase(GetCommitCaseRequest caseRequest) {
        List<CaseSearch> list = caseMapper.getCommitCase(caseRequest);
        return list;
    }

    @Override
    public JsonReturn deleteCase(Integer id) {
        int i = caseMapper.deleteCase(id);
        if (i<=0){
            return JsonReturn.fail(CommonStatus.FAULT);
        }
        return JsonReturn.success();
    }

    @Override
    public JsonReturn alterCase(Map<String, Object> jsonmap) {
        int status = caseMapper.alterCase(jsonmap);
        if (status <= 0){
            return JsonReturn.fail(CommonStatus.FAULT);
        }
        return JsonReturn.success();
    }

    @Override
    public JsonReturn getFeedback() {
        List<GetFeedbackRes> list = lawMapper.getFeedback();
        return JsonReturn.success(list);
    }


    @Override
    public List<Law> searchLaw(LawSearch lawSearch) {
        return lawMapper.searchLaw(lawSearch);
    }

    @Autowired
    private CaseMapper caseMapper;

    @Autowired
    private LawMapper lawMapper;

    @Autowired
    private SysMapper sysMapper;

    /**
     * 现行有效 失效 已被修改 尚未生效 部分失效
     * @return List<SideBar>
     */
    public List<SideBar> getTime() {
        //现行有效 失效 已被修改 尚未生效 部分失效
        List<SideBar> listTime = new LinkedList<>();
        SideBar sideBar = sysMapper.isNowInvalid();
        sideBar.setTitle("现行有效");
        listTime.add(sideBar);

        sideBar = sysMapper.isInvalid();
        sideBar.setTitle("失效");
        listTime.add(sideBar);

        sideBar = sysMapper.isAlter();
        sideBar.setTitle("已被修改");
        listTime.add(sideBar);

        sideBar = sysMapper.isNoInvalid();
        sideBar.setTitle("尚未生效");
        listTime.add(sideBar);

        sideBar = sysMapper.isInvalidPart();
        sideBar.setTitle("部分失效");
        listTime.add(sideBar);

        return listTime;
    }

    private List<SideBar> getTime(LawSearch lawSearch) {
        //现行有效 失效 已被修改 尚未生效 部分失效
        List<SideBar> listTime = new LinkedList<>();
        if(BzsyUtils.isNotEmpty(lawSearch.getTime())){
            if (lawSearch.getTime() == 1){
                SideBar sideBar = sysMapper.getSidebarInSearchTime(1,lawSearch);
                sideBar.setTitle("现行有效");
                listTime.add(sideBar);
            } else if (lawSearch.getTime() == 2){
                SideBar sideBar = sysMapper.getSidebarInSearchTime(2,lawSearch);
                sideBar.setTitle("尚未生效");
                listTime.add(sideBar);
            }else if (lawSearch.getTime() == 3){
                SideBar sideBar = sysMapper.getSidebarInSearchTime(3,lawSearch);
                sideBar.setTitle("失效");
                listTime.add(sideBar);
            }else if (lawSearch.getTime() == 4){
                SideBar sideBar = sysMapper.getSidebarInSearchTime(4,lawSearch);
                sideBar.setTitle("部分失效");
                listTime.add(sideBar);
            }else if (lawSearch.getTime() == 5){
                SideBar sideBar = sysMapper.getSidebarInSearchTime(5,lawSearch);
                sideBar.setTitle("已被修改");
                listTime.add(sideBar);
            }
            return listTime;
        }
        SideBar sideBar = sysMapper.getSidebarInSearchTime(1,lawSearch);
        sideBar.setTitle("现行有效");
        listTime.add(sideBar);

        sideBar = sysMapper.getSidebarInSearchTime(3,lawSearch);
        sideBar.setTitle("失效");
        listTime.add(sideBar);

        sideBar = sysMapper.getSidebarInSearchTime(5,lawSearch);
        sideBar.setTitle("已被修改");
        listTime.add(sideBar);

        sideBar = sysMapper.getSidebarInSearchTime(2,lawSearch);
        sideBar.setTitle("尚未生效");
        listTime.add(sideBar);

        sideBar = sysMapper.getSidebarInSearchTime(4,lawSearch);
        sideBar.setTitle("部分失效");
        listTime.add(sideBar);

        return listTime;
    }
}
