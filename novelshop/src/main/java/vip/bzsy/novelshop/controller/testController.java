package vip.bzsy.novelshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import vip.bzsy.novelshop.exception.CommonException;

@Controller
@RequestMapping("/test")
public class testController {

    @ResponseBody
    @RequestMapping("/")
    public String test(){
        return "haha";
    }

    @RequestMapping("/exception")
    public String exception() {
        int i = 1/0;
        return "exception";
    }

    @ResponseBody
    @RequestMapping("/common/exception")
    public String Commonexception() throws CommonException {
       throw  new CommonException("hhhh");
    }


}
