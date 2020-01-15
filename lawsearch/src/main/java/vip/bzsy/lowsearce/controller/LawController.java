package vip.bzsy.lowsearce.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vip.bzsy.lowsearce.common.Audience;
import vip.bzsy.lowsearce.common.JsonReturn;
import vip.bzsy.lowsearce.model.Law;
import vip.bzsy.lowsearce.model.vo.LawSearch;
import vip.bzsy.lowsearce.model.vo.SideBar;
import vip.bzsy.lowsearce.service.LawService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping(value = "/api/law",produces = "application/json;charset=UTF-8")
public class LawController {

    @RequestMapping(value = "/searchLaw" , method = RequestMethod.POST)
    public JsonReturn searchLaw(@RequestBody LawSearch lawSearch){
        PageHelper.startPage(lawSearch.getPage(),lawSearch.getRows());
        List<Law> list =  lawserviceImpl.searchLaw(lawSearch);
        PageInfo<Law> pageInfo = new PageInfo<Law>(list);
        JsonReturn jsonReturn = lawserviceImpl.getSidebarInSearch(lawSearch);
        map.clear();
        map.put("list",pageInfo.getList());
        map.put("total",pageInfo.getTotal());
        map.put("bar",jsonReturn.getData());
        return JsonReturn.success(map);
    }

    @RequestMapping(value = "/getLaw" , method = RequestMethod.GET)
    public JsonReturn getLaw(@RequestParam(name = "page",defaultValue = "1",required = true) Integer page,
                         @RequestParam(name = "rows",defaultValue = "15",required = false) Integer rows,
                         @RequestParam(name = "part",required = false) String part,
                         @RequestParam(name = "sort",required = false) String sort){
        PageHelper.startPage(page,rows);
        List<Law> list =  lawserviceImpl.getLaw(part,sort);
        PageInfo<Law> pageInfo = new PageInfo<Law>(list);
        return JsonReturn.success(pageInfo);
    }

    @Autowired
    private LawService lawserviceImpl;

    @Autowired
    private Audience audience;

    private Map<String,Object> map = new HashMap<String,Object>();
}
