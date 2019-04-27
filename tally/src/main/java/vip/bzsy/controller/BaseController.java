package vip.bzsy.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.bzsy.common.Audience;
import vip.bzsy.service.*;

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

    @Autowired
    public ExpenditureService expenditureService;

    @Autowired
    public IncomeService incomeService;

    @Autowired
    public RepaymentService repaymentService;

    @Autowired
    public SortTypeService sortTypeService;

    @Autowired
    public UserService userService;

    public Map<String,Object> map = new HashMap<>();

    @Autowired
    public Audience audience;

    @Value("${SALT}")
    public String SALT;

    @Value("${PASS_COUNT}")
    public Integer PASS_COUNT;

    @Value("${SECRET_KEY}")
    public String SECRET_KEY;

    @Value("${API_KEY}")
    public String API_KEY;

    @Value("${APP_ID}")
    public String APP_ID;
}
