package vip.bzsy.controller;

import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.bzsy.common.Audience;
import vip.bzsy.service.StatusService;
import vip.bzsy.service.UserService;
import vip.bzsy.service.WordService;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lyf
 * @create 2019-03-27 11:50
 */
@Slf4j
@RestController
@RequestMapping(value = "/api" ,produces = "application/json;charset=UTF-8")
public class BaseController {

    public  SimpleDateFormat dataformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public Integer getUid(HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        Integer userId = (Integer) claims.get("userId");
        return userId;
    }

    @Autowired
    public StatusService statusService;

    @Autowired
    public WordService wordService;

    @Autowired
    public UserService userService;

    public Map<String,Object> map = new HashMap<>();

    @Autowired
    public Audience audience;

    @Value("${SALT}")
    public String SALT;

    @Value("${PASS_COUNT}")
    public Integer PASS_COUNT;
}
