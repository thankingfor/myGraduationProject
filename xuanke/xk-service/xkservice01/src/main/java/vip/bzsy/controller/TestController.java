package vip.bzsy.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.bzsy.common.CommonResponse;
import vip.bzsy.mapper.TestMapper;
import vip.bzsy.model.Perclass;

import java.util.List;

/**
 * @author lyf
 * @create 2019-03-20 21:34
 */
@Slf4j
@RequestMapping("/api")
@RestController
public class TestController {

    @RequestMapping("/test")
    public CommonResponse hello(){
        return CommonResponse.success("测试成功");
    }

    @RequestMapping("/testMybatis")
    public CommonResponse testMybatis(){
        List<Perclass> allPerClass = testMapper.findAllPerClass();
        return CommonResponse.success(allPerClass);
    }

    @Autowired
    private TestMapper testMapper;
}
