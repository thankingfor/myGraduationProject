package vip.bzsy.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lyf
 * @since 2019-03-27
 */
@Slf4j
@RestController
@RequestMapping(value = "/api" ,produces = "application/json;charset=UTF-8")
public class SortTypeController extends BaseController {

}

