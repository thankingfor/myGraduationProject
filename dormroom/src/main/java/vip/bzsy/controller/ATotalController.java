package vip.bzsy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import vip.bzsy.common.*;
import vip.bzsy.model.Dormroom;
import vip.bzsy.model.Feedback;
import vip.bzsy.model.Survey;
import vip.bzsy.model.User;
import vip.bzsy.model.vo.DormFilterVo;
import vip.bzsy.model.vo.SurveyVo;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("all")
@Slf4j
@RestController
@RequestMapping(value = "/api", produces = "application/json;charset=UTF-8")
public class ATotalController extends BaseController {

    /**
     * 0.1测试
     *
     * @return
     */
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public CommonResponse test() {
        return CommonResponse.success("测试成功");
    }

    /**
     * 1.2 用户注册
     * url: /register
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public CommonResponse register(@RequestBody User user) {
        //查询是否重复
        User selectOne = user.selectOne(new QueryWrapper<User>().eq("account", user.getAccount()));
        if (CommonUtils.isNotEmpty(selectOne)) {
            return CommonResponse.fail("账户已经存在");
        }
        //密码加密
        user.setPassword(Md5Utils.encryptPassword(user.getPassword(), SALT, PASS_COUNT));
        boolean save = userService.save(user);
        if (!save) {
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    /**
     * 1.1 用户登录
     * url: /login
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public CommonResponse login(@RequestBody User user) {
        User one = userService.getOne(new QueryWrapper<User>()
                .eq("account", user.getAccount())
                .eq("password", Md5Utils.encryptPassword(user.getPassword(), SALT, PASS_COUNT))
        );
        if (CommonUtils.isEmpty(one)) {
            return CommonResponse.fail(CommonStatus.USER_LOGIN_ACCOUNT_FAULT);
        }
        //jwt操作
        map.clear();
        String jewToken = JwtHelper.createJWT(one.getId(),
                audience.getClientId(), audience.getName(), audience.getExpiresSecond(), audience.getBase64Secret());
        map.put("token", jewToken);
        return CommonResponse.success(map);
    }

    /**
     * 1.3 获取用户信息
     * url：/getUserInfo
     */
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    public CommonResponse getUserInfo(HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        Integer userId = (Integer) claims.get("userId");
        User user = userService.getById(userId);
        map.clear();
        map.put("uid", user.getId());
        map.put("name", user.getName());
        map.put("account", user.getAccount());
        map.put("identity", user.getIdentity());
        if (user.getRoom()==null)
            map.put("room", "");
        else
            map.put("room", user.getRoom());
        return CommonResponse.success(map);
    }

    /**
     * 2.1 提交调查问卷
     * url：/postSurvey
     */
    @RequestMapping(value = "/postSurvey", method = RequestMethod.POST)
    public CommonResponse postSurvey(@RequestBody SurveyVo vo, HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        Integer userId = (Integer) claims.get("userId");
        Survey one = surveyService.getOne(new QueryWrapper<Survey>().eq("uid", userId));
        Survey survey = new Survey();
        BeanUtils.copyProperties(vo,survey);
        survey.setUid(userId);
        survey.setGame(SurveyVo.ArraytoString(vo.getGame()));
        survey.setRead(SurveyVo.ArraytoString(vo.getRead()));
        survey.setVideo(SurveyVo.ArraytoString(vo.getVideo()));
        survey.setSport(SurveyVo.ArraytoString(vo.getSport()));
        survey.setMusic(SurveyVo.ArraytoString(vo.getMusic()));
        if (CommonUtils.isNotEmpty(one)){
            boolean updateById = survey.setId(one.getId()).updateById();
            if (!updateById){
                return CommonResponse.fail("调查文件修改失败");
            }
        } else {
            boolean insert = survey.insert();
            if (!insert){
                return CommonResponse.fail("调查文件提交失败");
            }
        }
        return CommonResponse.success();
    }

    /**
     * 2.2 获取筛选宿舍后的结果
     * url: /getFilter
     */
    @RequestMapping(value = "/getFilter", method = RequestMethod.GET)
    public CommonResponse getFilter(HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        Integer userId = (Integer) claims.get("userId");
        Survey survey = surveyService.getOne(new QueryWrapper<Survey>().eq("uid", userId));
        //List<Dormroom> dormrooms = dormroomService.list(new QueryWrapper<Dormroom>().lt("peopleNum", 6));
        List<Dormroom> dormrooms = dormroomService.list();
        List<DormFilterVo> vos = dormrooms.stream()
                .filter(dormroom -> isGetUp(dormroom.getGetUpTime(), survey.getGetUpTime()) && isGetUp(dormroom.getSleepTime(), survey.getSleepTime()))
                .map(dormroom -> {
                    DormFilterVo filterVo = new DormFilterVo();
                    filterVo.setRoomId(dormroom.getId());
                    filterVo.setPeopleNum(dormroom.getPeopleNum());
                    filterVo.setGetUpTime(SurveyVo.StringtoArray(dormroom.getGetUpTime()));
                    filterVo.setSleepTime(SurveyVo.StringtoArray(dormroom.getSleepTime()));
                    filterVo.setGamePct(getPec(dormroom.getGame(),survey.getGame(), dormroom.getPeopleNum()));
                    filterVo.setReadPct(getPec(dormroom.getRead(),survey.getRead(),dormroom.getPeopleNum()));
                    filterVo.setVideoPct(getPec(dormroom.getVideo(),survey.getVideo(),dormroom.getPeopleNum()));
                    filterVo.setSportPct(getPec(dormroom.getSport(),survey.getSport(),dormroom.getPeopleNum()));
                    filterVo.setMusicPctc(getPec(dormroom.getMusic(),survey.getMusic(),dormroom.getPeopleNum()));
                    filterVo.setReligionPct(getPec(dormroom.getReligion(),survey.getReligion().toString(),dormroom.getPeopleNum()));
                    filterVo.setQuitePct(getPec(dormroom.getQuite(),survey.getQuite().toString(),dormroom.getPeopleNum()));
                    filterVo.setNoisePct(getPec(dormroom.getNoise(),survey.getNoise().toString(),dormroom.getPeopleNum()));
                    filterVo.setSoundPct(getPec(dormroom.getSound(),survey.getSound().toString(),dormroom.getPeopleNum()));
                    filterVo.setStayPct(getPec(dormroom.getStay(),survey.getStay().toString(),dormroom.getPeopleNum()));
                    filterVo.setTotal();
                    return filterVo;
                })
                .sorted((x,y)->(int)(y.getTotal()-x.getTotal()))
                .collect(Collectors.toList());
        return CommonResponse.success(vos);
    }

    /**
     * 2.3 获取宿舍详情
     * url: /getRoomInfo
     */
    @RequestMapping(value = "/getRoomInfo", method = RequestMethod.GET)
    public CommonResponse getRoomInfo(Integer id, HttpServletRequest request) {
        //查找user表中这个宿舍的成员
        List<User> users = userService.list(new QueryWrapper<User>().eq("room", id));
        List<SurveyVo> surveyVos = users.stream()
                .map(user -> {
                    Survey survey = surveyService.getOne(new QueryWrapper<Survey>().eq("uid", user.getId()));
                    SurveyVo vo = new SurveyVo();
                    BeanUtils.copyProperties(survey,vo);
                    vo.setName(user.getName());
                    vo.setGame(SurveyVo.StringtoArray(survey.getGame()));
                    vo.setRead(SurveyVo.StringtoArray(survey.getRead()));
                    vo.setVideo(SurveyVo.StringtoArray(survey.getVideo()));
                    vo.setSport(SurveyVo.StringtoArray(survey.getSport()));
                    vo.setMusic(SurveyVo.StringtoArray(survey.getMusic()));
                    return vo;
                })
                .collect(Collectors.toList());
        List<Feedback> feedbacks = feedbackService.list(new QueryWrapper<Feedback>().eq("room_id", id));
        Map<Integer, List<User>> mapUsers = users.stream().collect(Collectors.groupingBy((User user) -> user.getId()));
        List<Map<String, String>> feedBackList = feedbacks.stream()
                .filter(feedback -> mapUsers.containsKey(feedback.getUid()))
                .map(feedback -> {
                    Map<String, String> maps = new HashMap<>();
                    maps.put("id", feedback.getId()+"");
                    maps.put("name", mapUsers.get(feedback.getUid()).get(0).getName());
                    maps.put("comment", feedback.getComment());
                    maps.put("harmonious", feedback.getHarmonious().toString());
                    return maps;
                }).collect(Collectors.toList());
        map.clear();
        map.put("memberList",surveyVos);
        map.put("feedBackList",feedBackList);
        return CommonResponse.success(map);
    }

    /**
     * 2.4 选择宿舍
     * url: /chooseRoom GET
     */
    @RequestMapping(value = "/chooseRoom", method = RequestMethod.GET)
    public CommonResponse chooseRoom(Integer id,HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        Integer userId = (Integer) claims.get("userId");
        log.info(userId+"");
        User user = new User().setId(userId).selectById();
        if (CommonUtils.isEmpty(user)){
            return CommonResponse.fail("用户为空");
        }
        boolean updateById = user.setRoom(id).updateById();
        if (!updateById){
            return CommonResponse.fail("用户分配失败");
        }else {

            Dormroom dormroom = dormroomService.getById(id);
            dormroom.setPeopleNum(dormroom.getPeopleNum()+1);
            //获取用户的调查表
            Survey survey = surveyService.getOne(new QueryWrapper<Survey>().eq("uid", userId));
            //修改宿舍的属性
            dormroom.setGame(dormroom.getGame()+","+survey.getGame());
            dormroom.setRead(dormroom.getRead()+","+survey.getRead());
            dormroom.setVideo(dormroom.getVideo()+","+survey.getVideo());
            dormroom.setSport(dormroom.getSport()+","+survey.getSport());
            dormroom.setMusic(dormroom.getMusic()+","+survey.getMusic());
            dormroom.setReligion(dormroom.getReligion()+","+survey.getReligion());
            dormroom.setQuite(dormroom.getQuite()+","+survey.getQuite());
            dormroom.setNoise(dormroom.getNoise()+","+survey.getNoise());
            dormroom.setSound(dormroom.getSound()+","+survey.getSound());
            dormroom.setStay(dormroom.getStay()+","+survey.getStay());
            boolean updateById1 = dormroom.updateById();
            if (!updateById1){
                return CommonResponse.fail("宿舍人数添加失败");
            }
        }
        return CommonResponse.success();
    }

    /**
     * 2.5 新建宿舍
     * url: /newRoom GET
     * 备注：后台读取该用户提交的调查问卷，新建一个宿舍，
     * 初始信息为该用户的作息时间段和喜好，如该用户在09:20起床，那宿舍的getUp字段的数据为[09:00, 09:59]
     */
    @RequestMapping(value = "/newRoom", method = RequestMethod.GET)
    public CommonResponse newRoom(HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        Integer userId = (Integer) claims.get("userId");
        Survey survey = surveyService.getOne(new QueryWrapper<Survey>().eq("uid", userId));
        Dormroom dormroom = new Dormroom();
        dormroom.setPeopleNum(1);//设置人数
        dormroom.setGetUpTime(getDormGetUp(survey.getGetUpTime()));//设置起床时间
        dormroom.setSleepTime(getDormGetUp(survey.getSleepTime()));
        dormroom.setGame(survey.getGame());
        dormroom.setRead(survey.getRead());
        dormroom.setVideo(survey.getVideo());
        dormroom.setSport(survey.getSport());
        dormroom.setMusic(survey.getMusic());

        dormroom.setReligion(survey.getReligion()+"");
        dormroom.setQuite(survey.getQuite()+"");
        dormroom.setNoise(survey.getNoise()+"");
        dormroom.setSound(survey.getSound()+"");
        dormroom.setStay(survey.getStay()+"");


        boolean insert = dormroom.insert();
        if (!insert){
            return CommonResponse.fail("宿舍创建失败");
        }
        new User().setId(userId).setRoom(dormroom.getId()).updateById();
        map.clear();
        map.put("roomId",dormroom.getId());
        return CommonResponse.success(map);
    }

    /**
     * 2.6 评论宿舍
     * url: /postComment POST
     */
    @RequestMapping(value = "/postComment", method = RequestMethod.POST)
    public CommonResponse postComment(@RequestBody Feedback feedback , HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        Integer userId = (Integer) claims.get("userId");
        Feedback one = feedbackService.getOne(new QueryWrapper<Feedback>().eq("uid", userId));
        feedback.setUid(userId);
        if (CommonUtils.isNotEmpty(one)){
            feedback.setId(one.getId());
            boolean updateById = feedback.updateById();
            if (!updateById){
                return CommonResponse.fail("评论修改失败");
            }
        } else {
            boolean insert = feedback.insert();
            if (!insert){
                return CommonResponse.fail("评论添加失败");
            }
        }
        return CommonResponse.success();
    }

    /**
     * 3.1 获取用户列表
     * url: /getUserList GET
     */
    @RequestMapping(value = "/getUserList", method = RequestMethod.GET)
    public CommonResponse getUserList() {
        List<User> list = userService.list();
        return CommonResponse.success(list);
    }

    /**
     * 3.2 修改用户信息
     * url: /modifyUserInfo POST
     */
    @RequestMapping(value = "/modifyUserInfo", method = RequestMethod.POST)
    public CommonResponse modifyUserInfo(@RequestBody User user) {
        if (CommonUtils.isNotEmpty(user.getPassword()))
            user.setPassword(Md5Utils.encryptPassword(user.getPassword(), SALT, PASS_COUNT));
        boolean b = user.updateById();
        if (!b){
            return CommonResponse.fail("修改用户失败");
        }
        return CommonResponse.success();
    }

    /**
     * 3.4 获取宿舍列表
     * url: /getRoomList GET
     */
    @RequestMapping(value = "/getRoomList", method = RequestMethod.GET)
    public CommonResponse getRoomList() {
        List<Dormroom> list = dormroomService.list();
        map.clear();
        List<Map<String,Object>> mapList = new ArrayList<>();
        list.stream().forEach(room->{
            Map<String,Object> maps  =  new HashMap<>();
            maps.put("id",room.getId());
            maps.put("getUpTime",SurveyVo.StringtoArray(room.getGetUpTime()));
            maps.put("sleepTime",SurveyVo.StringtoArray(room.getSleepTime()));
            mapList.add(maps);
        });
        return CommonResponse.success(mapList);
    }

    /**
     * 3.3 删除用户信息
     * url: /c GET
     */
    @RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
    public CommonResponse deleteUser(User user) {
        user.deleteById();
        return CommonResponse.success();
    }

    /**
     * 3.5 成员移出宿舍
     * url: /removeMember POST
     */
    @RequestMapping(value = "/removeMember", method = RequestMethod.POST)
    public CommonResponse removeMember(@RequestBody Map<String,Integer> maps) {
        Integer uid = maps.get("uid");
        Integer roomId = maps.get("roomId");
        dormroomService.removeMember(uid,roomId);
        return CommonResponse.success();
    }

    /**
     * 在时间的区间内[09:00, 09:59]返回true
     * 否则返回false
     */
    public boolean isGetUp(String str,String my){
        String[] split = str.split(",");
        String[] time1 = split[0].split(":");
        int start = Integer.valueOf(time1[0]) * 60 + Integer.valueOf(time1[1]);
        String[] time2 = split[1].split(":");
        int end = Integer.valueOf(time2[0]) * 60 + Integer.valueOf(time2[1]);
        String[] myTime = my.split(":");
        int myTm = Integer.valueOf(myTime[0]) * 60 + Integer.valueOf(myTime[1]);
        if (myTm <= end && myTm >= start){
            return true;
        }
        return false;
    }


    /**
     * 初始信息为该用户的作息时间段和喜好，如该用户在09:20起床，那宿舍的getUp字段的数据为[09:00, 09:59]
     * @param str
     * @return
     */
    public String getDormGetUp(String str){
        String[] split = str.split(":");
        return split[0] + ":" + "00" + "," + split[0] + ":" + "59";
    }

    /**
     * 通过一个,分割的字符串生成一个数组，一个寝室多少人
     * 然后比较
     */
    public double getPec(String source,String str,int roomNum){
        double pec = 0.0;
        String[] sources = source.split(",");
        String[] strs = str.split(",");
        if (strs.length<=0){
            return pec;
        }
        for (String play:strs){
            int i = 0;
            for (String all:sources){
                if (play.equals(all))
                    i++;
            }
            double per = (double) i/roomNum;
            pec += per;
        }
        pec = pec / strs.length;
        BigDecimal bigDecimal = BigDecimal.valueOf(pec);
        pec = bigDecimal.setScale(1,BigDecimal.ROUND_HALF_UP).doubleValue();
        return pec;
    }

//    public static void main(String[] args) {
//        ATotalController aTotalController = new ATotalController();
//        String source = "网络游戏,单机游戏,掌上游戏,手机游戏,吃鸡游戏,单机游戏,单机游戏";
//        String str = "网络游戏,单机游戏";
//        int roomNum = 5;
//        double pec = aTotalController.getPec(source, str, roomNum);
//        System.out.println(pec);
//    }

}
