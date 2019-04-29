package vip.bzsy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import vip.bzsy.common.*;
import vip.bzsy.model.Status;
import vip.bzsy.model.User;
import vip.bzsy.model.Word;
import vip.bzsy.model.vo.UserVo;
import vip.bzsy.model.vo.WordVo;

import javax.servlet.http.HttpServletRequest;
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
     *
     * @param user
     * @return
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
        if (CommonUtils.isEmpty(user.getIdentity())) {
            user.setIdentity("user");
        }
        boolean save = userService.save(user);
        if (!save) {
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    /**
     * 1.1 用户登录
     * url: /login
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public CommonResponse login(@RequestBody User user) {
        User one = userService.getOne(new QueryWrapper<User>()
                .eq("account", user.getAccount())
                .eq("password", Md5Utils.encryptPassword(user.getPassword(), SALT, PASS_COUNT))
        );
        if (CommonUtils.isEmpty(one)) {
            return new CommonResponse(2,"账号不存在");
        }
        if (one.getOkornot()==0){
            return new CommonResponse(3, "账号正在审核");
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
     *
     * @param request
     * @return
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
        return CommonResponse.success(map);
    }

    /**
     * 2.1 获取单词列表
     * url: /getWordList
     * 说明：获取对应词库的未被该用户标记的所有单词
     * /getWordList?unit=4
     * 获取四级单词
     */
    @RequestMapping(value = "/getWordList", method = RequestMethod.GET)
    public CommonResponse getWordList(Integer unit,HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        Integer userId = (Integer) claims.get("userId");
        //所有的四级词汇
        List<Word> wordList = wordService.list(new QueryWrapper<Word>().eq("unit", unit));
        //用户所有被标记的
        List<Status> statusList = statusService.list(new QueryWrapper<Status>().eq("uid", userId));
        List<Integer> listwords = statusList.stream().map(status -> status.getWordId()).collect(Collectors.toList());
        List listMap = new ArrayList();
        wordList.stream()
                .filter(word -> !listwords.contains(word.getId()))
                .forEach(word -> {
                    Map<String,Object> map = new HashMap<>();
                    map.put("id",word.getId());
                    map.put("word",word.getWord());
                    map.put("chinese",WordVo.setChineseStr(word.getChinese()));
                    listMap.add(map);
                });
        return CommonResponse.success(listMap);
    }

    /**
     * 2.2 获取单词状态表中status为对应值的单词
     * url: /getWordStatus
     * /getWordStatus?status=1
     */
    @RequestMapping(value = "/getWordStatus", method = RequestMethod.GET)
    public CommonResponse getWordStatus(Integer status,HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        Integer userId = (Integer) claims.get("userId");
        //用户所有被标记的
        List<Status> statusList = statusService.list(new QueryWrapper<Status>().eq("uid", userId).eq("status",status));

        List listMap = new ArrayList();
        statusList.forEach(stu -> {
            Word word = new Word().setId(stu.getWordId()).selectById();
            Map<String,Object> map = new HashMap<>();
            map.put("id",word.getId());
            map.put("word",word.getWord());
            map.put("chinese",WordVo.setChineseStr(word.getChinese()));
            listMap.add(map);
        });
        return CommonResponse.success(listMap);
    }

    /**
     * 2.3 将单词标记为对应状态
     * url: /postWordStatus
     * 说明：如果在数据库中没有查到对应的单词，也就是说在用户尚未背过该单词的情况下给该单词更改状态，
     * 就将这个单词添加到单词状态表中（此时做的是插入操作而不是修改操作），status字段和id字段为发送过去的数据。
     * 如果在数据库中查到对应的单词，就只修改status字段即可。
     */
    @RequestMapping(value = "/postWordStatus", method = RequestMethod.POST)
    public CommonResponse postWordStatus(@RequestBody Status status,HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        Integer userId = (Integer) claims.get("userId");
        log.info(status.getId()+","+status.getStatus()+","+userId);
        Status one = statusService.getOne(new QueryWrapper<Status>()
                .eq("word_id", status.getId()).eq("uid", userId));
        if (CommonUtils.isNotEmpty(one)){
            one.setStatus(status.getStatus());
            boolean updateById = one.updateById();
            if (!updateById){
                return CommonResponse.fail("单词状态修改失败！");
            }
        } else {
            one = new Status();
            one.setUid(userId).setStatus(status.getStatus()).setWordId(status.getId());
            boolean add = statusService.save(one);
            if (!add)
                return CommonResponse.fail("单词状态添加失败！");
        }
        return CommonResponse.success();
    }

    /**
     * 2.4 查询单词
     * url: /searchWord
     * /searchWord?key=china
     */
    @RequestMapping(value = "/searchWord", method = RequestMethod.GET)
    public CommonResponse searchWord(String key,HttpServletRequest request) {
        List<Word> list = wordService.list(new QueryWrapper<Word>()
                .and(wrapper -> wrapper.like("word", key).or().like("chinese", key)));
        List<WordVo> voList = new ArrayList();
        list.forEach(word -> {
            WordVo vo = new WordVo();
            BeanUtils.copyProperties(word,vo);
            vo.setChinese(WordVo.setChineseStr(word.getChinese()));
            voList.add(vo);
        });
        return CommonResponse.success(voList);
    }

    /**
     * 2.5 获取所有单词
     * url: /getAllWords
     * /searchWord?key=china
     */
    @RequestMapping(value = "/getAllWords", method = RequestMethod.GET)
    public CommonResponse getAllWords(String unit,HttpServletRequest request) {
        List<Word> list = wordService.list(new QueryWrapper<Word>().eq("unit",unit));
        List<WordVo> voList = new ArrayList();
        list.forEach(word -> {
            WordVo vo = new WordVo();
            BeanUtils.copyProperties(word,vo);
            vo.setChinese(WordVo.setChineseStr(word.getChinese()));
            voList.add(vo);
        });
        return CommonResponse.success(voList);
    }


    /**
     * 3.1 获取用户列表（身份为user）
     * url: /getUserList
     */
    @RequestMapping(value = "/getUserList", method = RequestMethod.GET)
    public CommonResponse getUserList() {
        List<User> list = userService.list(new QueryWrapper<User>().eq("identity", "user"));
        List listMap = new ArrayList();
        list.forEach(user -> {
            Map<String,Object> map = new HashMap<>();
            map.put("uid", user.getId());
            map.put("name", user.getName());
            map.put("account", user.getAccount());
            map.put("status",user.getOkornot());
            listMap.add(map);
        });
        return CommonResponse.success(listMap);
    }

    /**
     * 3.2 修改用户信息
     * url: /modifyUserInfo
     */
    @RequestMapping(value = "/modifyUserInfo", method = RequestMethod.POST)
    public CommonResponse addWord(@RequestBody UserVo userVo) {
        User user = new User();
        BeanUtils.copyProperties(userVo,user);
        user.setId(userVo.getUid());
        if (CommonUtils.isNotEmpty(user.getPassword()))
            user.setPassword(Md5Utils.encryptPassword(user.getPassword(), SALT, PASS_COUNT));
        boolean insert = user.updateById();
        if (!insert){
            return CommonResponse.fail("用户修改失败！");
        }
        return CommonResponse.success();
    }

    /**
     * 3.3 添加单词
     * url: /addWord
     */
    @RequestMapping(value = "/addWord", method = RequestMethod.POST)
    public CommonResponse addWord(@RequestBody WordVo wordVo) {
        Word word = new Word();
        BeanUtils.copyProperties(wordVo,word);
        word.setChinese(wordVo.getChineseStr());
        boolean insert = word.insert();
        if (!insert){
            return CommonResponse.fail("单词添加失败！");
        }
        return CommonResponse.success();
    }

    /**
     * 3.4 修改单词
     * url: /editWord
     */
    @RequestMapping(value = "/modifyWord", method = RequestMethod.POST)
    public CommonResponse editWord(@RequestBody WordVo wordVo) {
        Word word = new Word();
        BeanUtils.copyProperties(wordVo,word);
        word.setChinese(wordVo.getChineseStr());
        boolean updateById = word.updateById();
        if (!updateById){
            return CommonResponse.fail("单词修改失败！");
        }
        return CommonResponse.success();
    }

    /**
     * 3.5 删除单词
     * url: /deleteWord
     */
    @RequestMapping(value = "/deleteWord", method = RequestMethod.GET)
    public CommonResponse deleteWord(Word word) {
        word.deleteById();
        return CommonResponse.success();
    }

    /**
     * 3.6 删除用户
     * url: /deleteUser
     */
    @RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
    public CommonResponse deleteUser(User user) {
        user.deleteById();
        return CommonResponse.success();
    }

    /**
     * 3.7 获取待审核用户
     * url: /getWaitUser
     */
    @RequestMapping(value = "/getWaitUser", method = RequestMethod.GET)
    public CommonResponse getWaitUser() {
        List<User> list = userService.list(new QueryWrapper<User>()
                .eq("identity", "user").eq("okornot",0));
        List listMap = new ArrayList();
        list.forEach(user -> {
            Map<String,Object> map = new HashMap<>();
            map.put("uid", user.getId());
            map.put("name", user.getName());
            map.put("account", user.getAccount());
            map.put("status",user.getOkornot());
            listMap.add(map);
        });
        return CommonResponse.success(listMap);
    }

    /**
     * 3.8 通过审核
     * url: /access
     */
    @RequestMapping(value = "/access", method = RequestMethod.GET)
    public CommonResponse access(User user) {
        boolean updateById = user.setOkornot(1).updateById();
        if (!updateById){
            return CommonResponse.fail("审核失败");
        }
        return CommonResponse.success();
    }
}
