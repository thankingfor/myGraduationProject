package vip.bzsy.lowsearce.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.bzsy.lowsearce.common.Audience;
import vip.bzsy.lowsearce.common.JsonReturn;
import vip.bzsy.lowsearce.model.Law;
import vip.bzsy.lowsearce.service.LawService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "/api/law",produces = "application/json;charset=UTF-8")
public class SysController {

    @RequestMapping(value = "/getSidebar" , method = RequestMethod.GET)
    public JsonReturn getSidebar(){
        return lawserviceImpl.getSidebar();
    }

    @Autowired
    private LawService lawserviceImpl;

    @Autowired
    private Audience audience;

    private Map<String,Object> map = new HashMap<String,Object>();
}

