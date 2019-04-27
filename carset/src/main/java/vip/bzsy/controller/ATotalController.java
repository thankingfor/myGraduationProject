package vip.bzsy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.tools.corba.se.idl.constExpr.Or;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.web.bind.annotation.*;
import vip.bzsy.common.*;
import vip.bzsy.model.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lyf
 * @create 2019-03-27 12:18
 */
@Slf4j
@RestController
@RequestMapping(value = "/api" ,produces = "application/json;charset=UTF-8")
public class ATotalController extends BaseController {

    /**
     * 0.1测试
     * @return
     */
    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public CommonResponse test(){
        return CommonResponse.success("测试成功");
    }

    /**
     * 1.2 用户注册
     * url: /register
     * @param user
     * @return
     */
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public CommonResponse register(@RequestBody User user){
        //查询是否重复
        User selectOne = user.selectOne(new QueryWrapper<User>().eq("account",user.getAccount()));
        if (CommonUtils.isNotEmpty(selectOne)){
            return CommonResponse.fail("账户已经存在");
        }
        //密码加密
        user.setPassword(Md5Utils.encryptPassword(user.getPassword(),SALT,PASS_COUNT));
        user.setCreated(new Date().getTime());
        if (CommonUtils.isEmpty(user.getIdentity())){
            user.setIdentity("user");
        }
        boolean save = userService.save(user);
        if (!save){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    /**
     * 1.1 用户登录
     * url: /login
     * @param user
     * @return
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public CommonResponse login(@RequestBody User user){
        User one = userService.getOne(new QueryWrapper<User>()
                .eq("account", user.getAccount())
                .eq("password", Md5Utils.encryptPassword(user.getPassword(), SALT, PASS_COUNT))
                );
        if (CommonUtils.isEmpty(one)){
            return CommonResponse.fail(CommonStatus.USER_LOGIN_ACCOUNT_FAULT);
        }
        //jwt操作
        map.clear();
        String jewToken = JwtHelper.createJWT(one.getId(),
                audience.getClientId(), audience.getName(),audience.getExpiresSecond(), audience.getBase64Secret());
        map.put("token",jewToken);
        return CommonResponse.success(map);
    }

    /**
     *1.3 获取用户信息
     * url：/userInfo
     * @param request
     * @return
     */
    @RequestMapping(value = "/userInfo",method = RequestMethod.GET)
    public CommonResponse getUserInfo(HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        Integer userId = (Integer) claims.get("userId");
        User user = userService.getById(userId);
        map.clear();
        map.put("uid",user.getId());
        map.put("name",user.getName());
        map.put("account",user.getAccount());
        map.put("identity",user.getIdentity());
        map.put("money",user.getMoney());
        return CommonResponse.success(map);
    }

    /**
     * 2.0 [+] 获取员工列表
     * url：/getStaffList GET
     * @return
     */
    @RequestMapping(value = "/getStaffList",method = RequestMethod.GET)
    public CommonResponse getStaffList(){
        List<User> list = userService.list(new QueryWrapper<User>().eq("identity", "staff"));
        List<Map<String, Object>> collect = list.stream().map(user -> {
            Map<String,Object> map = new LinkedHashMap<>();
            map.clear();
            map.put("id", user.getId());
            map.put("account", user.getAccount());
            map.put("name", user.getName());
            return map;
        }).collect(Collectors.toList());
        return CommonResponse.success(collect);
    }

    /**
     * 2.1 添加员工
     * url：/addStaff POST
     * @param request
     * @return
     */
    @RequestMapping(value = "/addStaff",method = RequestMethod.POST)
    public CommonResponse addStaff(@RequestBody User user,HttpServletRequest request){
        user.setIdentity("staff");
        return register(user);
    }

    /**
     * 2.2 添加vip活动
     * url：/addActivity POST
     * @return
     */
    @RequestMapping(value = "/addActivity",method = RequestMethod.POST)
    public CommonResponse addActivity(@RequestBody Activity activity){
        activity.setCreated(getNowTime());
        boolean insert = activity.insert();
        if (!insert){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    /**
     *2.2.1 删除vip活动
     * url：/deleteActive GET
     * @return
     */
    @RequestMapping(value = "/deleteActive",method = RequestMethod.GET)
    public CommonResponse deleteActive(Activity activity){
        if (!activity.deleteById()){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    /**
     * 2.3 添加停车场
     * url：/addParking POST
     * @return
     */
    @RequestMapping(value = "/addParking",method = RequestMethod.POST)
    public CommonResponse addParking(@RequestBody Parking parking){
        parking.setCreated(getNowTime());
        parking.insert();
        //根据count生成停车位
        if (CommonUtils.isNotEmpty(parking.getCount())){
            for (int i = 0 ;i < parking.getCount() ;i++){
                new Carset().setParkingId(parking.getId())
                        .setCreated(getNowTime()).setStatus(0).insert();
            }
        }
        return CommonResponse.success();
    }

    /**
     * 2.4 删除停车场
     * url：/deleteParking GET
     * @param parking
     * @return
     */
    @RequestMapping(value = "/deleteParking",method = RequestMethod.GET)
    public CommonResponse deleteParking(Parking parking){
        parking.deleteById();
        return CommonResponse.success();
    }

    /**
     * 2.5 修改停车场信息
     * url: /modifyParkingInfo POST
     * @param parking
     * @return
     */
    @RequestMapping(value = "/modifyParkingInfo",method = RequestMethod.POST)
    public CommonResponse modifyParkingInfo(@RequestBody Parking parking){
        parking.updateById();
        return CommonResponse.success();
    }

    /**
     * 2.6 修改车位状态
     * url: /modifySpaceStatus GET
     * @return
     */
    @RequestMapping(value = "/modifySpaceStatus",method = RequestMethod.GET)
    public CommonResponse modifySpaceStatus(Carset carset){
        carset.updateById();
        return CommonResponse.success();
    }

    /**
     * 2.7 获取用户列表
     * 备注：获取identity为user和vip的
     * url：/getUserList GET
     * @return
     */
    @RequestMapping(value = "/getUserList",method = RequestMethod.GET)
    public CommonResponse getUserList(){
        List<User> list = userService.list(new QueryWrapper<User>()
                .and(wapper -> wapper.eq("identity", "user").or().eq("identity", "vip")));
        return CommonResponse.success(list);
    }

    /**
     * 2.8 修改某用户信息
     * url：/modifyUserInfo POST
     * @return
     */
    @RequestMapping(value = "/modifyUserInfo",method = RequestMethod.POST)
    public CommonResponse modifyUserInfo(@RequestBody User user){
        boolean b = user.updateById();
        if (!b){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    /**
     * 2.9 获取反馈信息
     * url：/getFeedback
     * @return
     */
    @RequestMapping(value = "/getFeedback",method = RequestMethod.GET)
    public CommonResponse getFeedback(){
        List<Feedback> list = feedbackService.list();
        List<Map<String, Object>> listMap = list.stream().map(feedback -> {
            Map<String, Object> map = new HashMap<>();
            User user = new User().setId(feedback.getUid()).selectById();
            map.put("id", feedback.getId());
            map.put("content",feedback.getContent());
            map.put("name",user.getName());
            map.put("account",user.getAccount());
            return map;
        }).collect(Collectors.toList());
        return CommonResponse.success(listMap);
    }

    /**
     * 2.10 删除某用户（可以删除用户也可以删除员工）
     * url: /deleteUser GET
     * @return
     */
    @RequestMapping(value = "/deleteUser",method = RequestMethod.GET)
    public CommonResponse deleteUser(User user){
        boolean b = user.deleteById();
        return CommonResponse.success();
    }

    /**
     * 2.11 获取退单列表
     * url: /getBackOrder GET
     * 说明： 获取状态为3的退单列表
     * @return
     */
    @RequestMapping(value = "/getBackOrder",method = RequestMethod.GET)
    public CommonResponse getBackOrder(){
        List<Order> status = orderService.list(new QueryWrapper<Order>().eq("status", 3));
        List<Map<String, Object>> list = new LinkedList<>();
        for (Order order:status){
            Carset carset = new Carset().setId(order.getSpaceName()).selectById();
            Parking parking = new Parking().setId(carset.getParkingId()).selectById();
            Map<String, Object> map = new HashMap<>();
            map.put("id",order.getId());
            map.put("spaceName",carset.getId());
            map.put("parkingName",parking.getParkingName());
            map.put("startTime",order.getStartTime());
            map.put("timeLength",order.getTimeLength());
            map.put("cost",order.getCost());
            map.put("status",order.getStatus());
            list.add(map);
        }
        return CommonResponse.success(list);
    }

    /**
     * 2.12 修改订单状态
     * url：/modifyOrderState POST
     * @return
     */
    @RequestMapping(value = "/modifyOrderState",method = RequestMethod.POST)
    public CommonResponse modifyOrderState(@RequestBody Order order){
        if (order.getStatus()==4){
            Order order1 = order.selectById();
            User user = new User().setId(order1.getUid()).selectById();
            BigDecimal cost = BigDecimal.valueOf(order1.getTimeLength()).multiply(order1.getCost());
            BigDecimal money = user.getMoney().add(cost);
            user.setMoney(money).updateById();
        }
        order.updateById();
        return CommonResponse.success();
    }

    /**
     * 3.1 获取vip活列表
     * url: /getActivityList
     * @return
     */
    @RequestMapping(value = "/getActivityList",method = RequestMethod.GET)
    public CommonResponse getActivityList(){
        List<Activity> list = activityService.list();
        return CommonResponse.success(list);
    }

    /**
     * 3.1 获取vip活动
     * url: /getActivity
     * @return
     */
    @RequestMapping(value = "/getActivity",method = RequestMethod.GET)
    public CommonResponse getActivity(Activity activity){
        Activity activity1 = activity.selectById();
        return CommonResponse.success(activity1);
    }

    /**
     * 3.2 获取停车场列表
     * url：/getParking GET
     * @return
     */
    @RequestMapping(value = "/getParkingList",method = RequestMethod.GET)
    public CommonResponse getParking(@RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "6") Integer rows){
        IPage<Parking> pageInfo = parkingService.page(new Page<Parking>(page, rows), new QueryWrapper<Parking>());
        map.clear();
        map.put("total",pageInfo.getTotal());
        map.put("parkingList",pageInfo.getRecords());
        return CommonResponse.success(map);
    }

    /**
     * 3.3 获取停车位
     * url：/getSpaceList GET
     * @return
     */
    @RequestMapping(value = "/getSpaceList",method = RequestMethod.GET)
    public CommonResponse getSpaceList(@RequestParam(value = "id") Integer parkingId){
        List<Carset> list = carsetService.list(new QueryWrapper<Carset>().eq("parking_id", parkingId));
        return CommonResponse.success(list);
    }

    /**
     * 3.4 提交订单
     * url: /submitOrder POST
     * @return
     */
    @RequestMapping(value = "/submitOrder",method = RequestMethod.POST)
    public CommonResponse submitOrder(@RequestBody Order order,HttpServletRequest request) throws SchedulerException {
        //判断订单是否合法
        log.info("开始处理"+order);
        //预约时间和当前时间是否一样
        if (new Date().getTime() > order.getStartTime()){
            return CommonResponse.fail("订单开始时间大于当前时间，不合法");
        }
        //找出已经生效的订单 就是正在生效的订单 status = 1 or 2
        List<Order> listStatus = orderService.list(new QueryWrapper<Order>()
                .eq("space_name", order.getSpaceName())
                .between("status",1,2));

        for (Order old:listStatus){
            //判断是否合法
            if (!check(old,order)){
                map.clear();
                map.put("startTime",old.getStartTime());
                map.put("timeLength",old.getTimeLength());
                return new CommonResponse(2,"车位正在使用",map);
            }
        }
        log.info("已经合法");
        //开始处理业务
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        Integer userId = (Integer) claims.get("userId");

        order.setUid(userId);
        order.setCreated(new Date().getTime());
        order.setStatus(1);
        order.insert();
        Integer orderId = order.getId();
        //扣钱
        User user = new User().setId(userId).selectById();
        BigDecimal cost = BigDecimal.valueOf(order.getTimeLength()).multiply(order.getCost());
        BigDecimal money = user.getMoney().subtract(cost);
        user.setMoney(money).updateById();
        //创建触发器 等事件到了就把执行留一个任务 把status改为2
        Date startDate = new Date();
        startDate.setTime(order.getStartTime());
        JobDetail jobDetail = JobBuilder.newJob(JobPre.class)
                .withIdentity(orderId+"A",order.getUid()+"")
                .build();
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(orderId+"A",order.getUid()+"")
                .usingJobData("userId",userId)
                .usingJobData("orderId",orderId)
                .startAt(startDate)
                .build();
        Scheduler scheduler = schedulerFactory.getScheduler();
        scheduler.scheduleJob(jobDetail,trigger);
        scheduler.start();
        return CommonResponse.success("租借成功");
    }

    /**
     * 3.5 获取用户的订单列表
     * url：/getOrderList
     * @return
     */
    @RequestMapping(value = "/getOrderList",method = RequestMethod.GET)
    public CommonResponse getOrderList(HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        Integer userId = (Integer) claims.get("userId");
        List<Order> status = orderService.list(new QueryWrapper<Order>().eq("uid",userId));
        List<Map<String, Object>> list = new LinkedList<>();
        for (Order order:status){
            Carset carset = new Carset().setId(order.getSpaceName()).selectById();
            Parking parking = new Parking().setId(carset.getParkingId()).selectById();
            Map<String, Object> map = new HashMap<>();
            map.put("id",order.getId());
            map.put("spaceName",carset.getId());
            map.put("parkingName",parking.getParkingName());
            map.put("startTime",order.getStartTime());
            map.put("timeLength",order.getTimeLength());
            map.put("cost",order.getCost());
            map.put("status",order.getStatus());
            list.add(map);
        }
        return CommonResponse.success(list);
    }



    /**
     * 3.6 申请退单
     * url：/applyBackOrder GET
     * @return
     */
    @RequestMapping(value = "/applyBackOrder",method = RequestMethod.GET)
    public CommonResponse applyBackOrder(Order order) throws SchedulerException {
        Order order1 = order.selectById();
        order1.setStatus(3).updateById();
        new Carset().setId(order1.getSpaceName()).setStatus(0).updateById();
        Scheduler scheduler = schedulerFactory.getScheduler();
        Set<TriggerKey> triggerKeys = scheduler.getTriggerKeys(GroupMatcher.anyGroup());
        for (TriggerKey triggerKey:triggerKeys){
            if (triggerKey.getGroup().equals(order1.getUid()+"")){
                if (triggerKey.getName().equals(order1.getId()+"A")){
                    scheduler.pauseTrigger(triggerKey);// 停止触发器
                    scheduler.unscheduleJob(triggerKey);// 移除触发器
                    scheduler.deleteJob(JobKey.jobKey(order1.getId()+"A", order1.getUid()+""));// 删除任务
                }
            }
        }
        return CommonResponse.success();
    }

    /**
     * 3.7 账户充值
     * url：/recharge POST
     */
    @RequestMapping(value = "/recharge",method = RequestMethod.POST)
    public CommonResponse recharge(@RequestBody User user) {
        User byId = user.selectById();
        BigDecimal money = user.getMoney();
        if (byId.getMoney()==null){
            byId.setMoney(money).updateById();
        }else {
            byId.setMoney(byId.getMoney().add(money)).updateById();
        }
        return CommonResponse.success();
    }

    /**
     * 3.8 充值为Vip
     * url: /tobeVip GET
     */
    @RequestMapping(value = "/tobeVip",method = RequestMethod.GET)
    public CommonResponse tobeVip(User user) {
        User byId = user.selectById();
        BigDecimal money = byId.getMoney();
        if (money == null){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        BigDecimal vipcost =  new BigDecimal(100);
        if (vipcost.compareTo(money) == 1){
            return CommonResponse.fail("余额不足");
        }
        user.setMoney(money.subtract(vipcost));
        user.setIdentity("vip").updateById();
        return CommonResponse.success();
    }

    /**
     * 3.9 用户反馈
     * url: /submitFeedback POST
     */
    @RequestMapping(value = "/submitFeedback",method = RequestMethod.POST)
    public CommonResponse submitFeedback(@RequestBody Feedback feedback,HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        Integer userId = (Integer) claims.get("userId");
        feedback.setUid(userId);
        feedback.setCreated(new Date().getTime());
        boolean insert = feedback.insert();
        if (!insert){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    @RequestMapping(value = "/getAllJob",method = RequestMethod.GET)
    public CommonResponse getAllJob() throws SchedulerException {
        Scheduler scheduler = schedulerFactory.getScheduler();
        Set<JobKey> jobKeySet = scheduler.getJobKeys(GroupMatcher.anyGroup());
        return CommonResponse.success(jobKeySet.toString());
    }

    /**
     * 判断用户是否合法
     * @param now
     * @param old
     * @return
     */
    private boolean check(Order old, Order now) {
        //一小时
        Long oneHour = 1 * 60 * 60 * 1000L;
        Long startTime = old.getStartTime();
        Long endTime = startTime + old.getTimeLength() * oneHour;
        Long nowStartTime = now.getStartTime();
        Long nowEndTime = nowStartTime + now.getTimeLength() * oneHour;
        if (endTime < nowStartTime || nowEndTime < startTime){
            return true;
        }
        return false;
    }


    public Long getNowTime(){
        return new Date().getTime();
    }

}
