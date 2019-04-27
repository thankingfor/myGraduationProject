package vip.bzsy.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.bzsy.common.Audience;
import vip.bzsy.service.*;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * @author lyf
 * @create 2019-03-27 11:50
 */
@Slf4j
@RestController
@RequestMapping(value = "/api" ,produces = "application/json;charset=UTF-8")
public class BaseController {

    public SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static SchedulerFactory schedulerFactory = new StdSchedulerFactory();

    @Autowired
    public ActivityService activityService;

    @Autowired
    public CarsetService carsetService;

    @Autowired
    public FeedbackService feedbackService;

    @Autowired
    public OrderService orderService;

    @Autowired
    public ParkingService parkingService;

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
