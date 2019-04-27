package vip.bzsy.lowsearce.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author lyf
 * @create 2019-03-15 21:21
 */
@Controller
public class PathController {

    @RequestMapping("/")
    public String index(){

        return "index";
    }
}
