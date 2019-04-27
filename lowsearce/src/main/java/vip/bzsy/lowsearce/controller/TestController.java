package vip.bzsy.lowsearce.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/test",produces = "application/json;charset=UTF-8")
public class TestController {


    @RequestMapping(value = {"/hello","/"})
    public String hello(){
        return "你好";
    }


    @RequestMapping("/exception")
    public String exception(){
        int i = 1 / 0;
        return "index";
    }
}
