package vip.bzsy.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.stereotype.Controller;
import vip.bzsy.common.CommonResponse;
import vip.bzsy.common.CommonStatus;
import vip.bzsy.model.Faculity;
import vip.bzsy.model.User;
import vip.bzsy.service.FaculityService;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lyf
 * @since 2019-03-22
 */
@Slf4j
@RestController
@RequestMapping(value = "/api" ,produces = "application/json;charset=UTF-8")
public class FaculityController {

    @RequestMapping(value = "/addFaculty",method = RequestMethod.POST)
    public CommonResponse addFaculity(@RequestBody Map<String,Object> map){
        Faculity faculity = new Faculity();
        faculity.setName((String) map.get("faculityName"));
        boolean save = faculityService.save(faculity);
        if (!save){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    @RequestMapping(value = "/getFaculty",method = RequestMethod.GET)
    public CommonResponse getFaculty(){
        List<Faculity> list = faculityService.list();
//        String[] arr = new String[list.size()];
//        for (int i = 0;i < list.size();i++){
//            arr[i] = list.get(i).getName();
//        }
        return CommonResponse.success(list);
    }


    private Map<String,Object> map = new LinkedHashMap<>();

    @Autowired
    private FaculityService faculityService;
}

