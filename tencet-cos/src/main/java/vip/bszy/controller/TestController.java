package vip.bszy.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.bszy.config.TencentCosConfig;

import javax.annotation.Resource;

/**
 * @author ：李延富
 * @date ：Created in 2019/12/17 22:35
 * description ：
 */
@Slf4j
@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    private TencentCosConfig cosConfig;

    @RequestMapping("/aaa")
    public String test() {
        log.info(cosConfig.getSecretId());
        log.info(cosConfig.getSecretKey());
        return "";
    }
}
