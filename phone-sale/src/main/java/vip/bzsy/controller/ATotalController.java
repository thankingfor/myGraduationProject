package vip.bzsy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import vip.bzsy.common.*;
import vip.bzsy.model.*;
import vip.bzsy.model.vo.CartVo;
import vip.bzsy.model.vo.PublishItem;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lyf
 * @create 2019-03-27 12:18
 */
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
    public CommonResponse register(@RequestBody XUser user) {
        //查询是否重复
        XUser selectOne = user.selectOne(new QueryWrapper<XUser>().eq("account", user.getAccount()));
        if (CommonUtils.isNotEmpty(selectOne)) {
            return CommonResponse.fail("账户已经存在");
        }
        //密码加密
        user.setPassword(Md5Utils.encryptPassword(user.getPassword(), SALT, PASS_COUNT));
        if (CommonUtils.isEmpty(user.getIdentity())) {
            user.setIdentity("user");
        }
        user.setMoney(new BigDecimal(0));
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
    public CommonResponse login(@RequestBody XUser user) {
        XUser one = userService.getOne(new QueryWrapper<XUser>()
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
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/getUserInfo", method = RequestMethod.GET)
    public CommonResponse getUserInfo(HttpServletRequest request) {
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        Integer userId = (Integer) claims.get("userId");
        XUser user = userService.getById(userId);
        map.clear();
        map.put("receive", user.getReceive());
        map.put("uid", user.getId());
        map.put("name", user.getName());
        map.put("account", user.getAccount());
        map.put("identity", user.getIdentity());
        map.put("money", user.getMoney());
        return CommonResponse.success(map);
    }

    /**
     * 1.1 获取商品列表
     * url：/getItemList GET
     */
    @RequestMapping(value = "/getItemList", method = RequestMethod.GET)
    public CommonResponse getItemList(@RequestParam(value = "page",defaultValue = "1") Integer page,
                                      @RequestParam(value = "rows",defaultValue = "9") Integer rows,
                                      @RequestParam(value = "keyWords",defaultValue = "")String keyWords,
                                      HttpServletRequest request) {
        IPage<XItem> page1 = itemService.page(new Page<XItem>(page, rows), new QueryWrapper<XItem>()
                .like("title", keyWords).orderByDesc("`time`"));
        map.clear();
        map.put("total",page1.getTotal());
        map.put("itemList",page1.getRecords());
        return CommonResponse.success(map);
    }

    /**
     * 1.2 获取商品详情
     * url：/getItem GET
     */
    @RequestMapping(value = "/getItem", method = RequestMethod.GET)
    public CommonResponse getItem(XItem item,HttpServletRequest request) {
        XItem xItem = item.selectById();
        PublishItem publishItem = new PublishItem();
        BeanUtils.copyProperties(xItem,publishItem);
        publishItem.setColorStr(xItem.getColor());
        publishItem.setSortStr(xItem.getSort());
        return CommonResponse.success(publishItem);
    }

    /**
     * 1.3 获取订单列表
     * url：getOrderList GET
     * /getOrderList?page=1&uid=1&status=1
     */
    @RequestMapping(value = "/getOrderList", method = RequestMethod.GET)
    public CommonResponse getOrderList(@RequestParam(value = "page",defaultValue = "1") Integer page,
                                       @RequestParam(value = "rows",defaultValue = "10") Integer rows,
                                       @RequestParam(value = "uid",defaultValue = "-1") Integer uid,
                                       @RequestParam(value = "status",defaultValue = "-1") Integer status,
                                       HttpServletRequest request) {
        QueryWrapper<XOrder> xOrderQueryWrapper = new QueryWrapper<>();
        if (uid!=-1){
            xOrderQueryWrapper.eq("uid",uid);
        }
        if (status!=-1){
            xOrderQueryWrapper.eq("status",status);
        }
        xOrderQueryWrapper.orderByDesc("`time`");
        IPage<XOrder> page1 = orderService.page(new Page<XOrder>(page, rows), xOrderQueryWrapper);
        List<Map<String, Object>> collect = page1.getRecords().stream().map(order -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id",order.getId());
            m.put("uid",order.getUid());
            XUser user = new XUser().setId(order.getUid()).selectById();
            m.put("userName",user.getName());
            m.put("itemId",order.getItemId());
            XItem xItem = new XItem().setId(order.getItemId()).selectById();
            m.put("title",xItem.getTitle());
            m.put("cover",xItem.getCover());
            m.put("color",order.getColor());
            m.put("sort",order.getSort());
            m.put("price",order.getPrice());
            m.put("status",order.getStatus());
            m.put("delivery",order.getDelivery());
            m.put("time",order.getTime());
            m.put("receive",order.getReceive());
            return m;
        }).collect(Collectors.toList());
        map.clear();
        map.put("total",page1.getTotal());
        map.put("orderList",collect);
        return CommonResponse.success(map);
    }

    /**
     * 1.4 获取商品评论
     * url：/getComment?id=1 GET
     */
    @RequestMapping(value = "/getComment", method = RequestMethod.GET)
    public CommonResponse getComment(XItem xItem, HttpServletRequest request) {
        List<XEvaluate> list = evaluateService.list(new QueryWrapper<XEvaluate>().eq("item_id", xItem.getId()));
        List<Map<String, Object>> collect = list.stream().map(evaluate -> {
            XEvaluate xEvaluate = evaluate.selectById();
            XUser user = new XUser().setId(xEvaluate.getUid()).selectById();
            Map<String, Object> m = new HashMap<>();
            m.put("id", xEvaluate.getId());
            m.put("name", user.getName());
            m.put("star", xEvaluate.getStar());
            m.put("content", xEvaluate.getContent());
            m.put("time", xEvaluate.getTime());
            return m;
        }).collect(Collectors.toList());
        return CommonResponse.success(collect);
    }

    /**
     * 2.1 账户充值
     * url：/recharge GET
     */
    @RequestMapping(value = "/recharge", method = RequestMethod.GET)
    public CommonResponse recharge(Double money, HttpServletRequest request) {
        totalService.recharge(getUid(request), money);
        return CommonResponse.success();
    }

    /**
     * 2.2 生成订单（购买商品）
     * url：/createOrder
     * 注意：生成1个订单后，把该商品对应的saleNum（销售量）+1
     */
    @RequestMapping(value = "/createOrder", method = RequestMethod.POST)
    public CommonResponse createOrder(@RequestBody XOrder order, HttpServletRequest request) {
        XItem xItem = new XItem().setId(order.getItemId()).selectById();
        if (CommonUtils.isEmpty(xItem)){
            return CommonResponse.fail("商品不存在");
        }
        order.setStatus(1);//未发货
        order.setUid(getUid(request));
        boolean insert = order.insert();
        if (!insert)
            return CommonResponse.fail(CommonStatus.FAULT);
        xItem.setStock(xItem.getStock()-1).setSaleNum(xItem.getSaleNum() + order.getCount()).updateById();
        XUser user = new XUser().setId(getUid(request)).selectById();
        BigDecimal money = user.getMoney().subtract(order.getPrice());
        user.setMoney(money).updateById();
        return CommonResponse.success();
    }

    /**
     * 2.3 确认收货
     * 备注：订单状态由2改为3
     * url：/receiveOrder GET
     */
    @RequestMapping(value = "/receiveOrder", method = RequestMethod.GET)
    public CommonResponse receiveOrder(XOrder order, HttpServletRequest request) {
        boolean insert = order.setStatus(3).updateById();//确认收货
        if (!insert)
            return CommonResponse.fail(CommonStatus.FAULT);
        return CommonResponse.success();
    }

    /**
     * 2.4 申请退货
     * 备注：商品状态由3改为4
     * url：/backOrder GET
     */
    @RequestMapping(value = "/backOrder", method = RequestMethod.GET)
    public CommonResponse backOrder(XOrder order, HttpServletRequest request) {
        boolean insert = order.setStatus(4).updateById();//申请退货
        if (!insert)
            return CommonResponse.fail(CommonStatus.FAULT);
        return CommonResponse.success();
    }

    /**
     * 2.5 添加购物车
     * url：/addCart POST
     * 注意：当用户把一件商品选择好了颜色和规格点击加入购物车时，后台需要检索用户添加的这件商品的所选规格和颜色，是否已存在在于该用户的购物车中，具体的对比方法为：
     * 对比uid itemId color sort这三个字段是否与购物车表中的某条数据重复
     * 如果有重复，就说明用户已经把这件商品加入过购物车（如用户购物车里已存在了一台“红色8G+128G小米9”，此时用户再向购物车添加一台红色8+128G的小米9），不新增数据，只增加已有数据的count字段（把原来的1台改为2台）；
     * 如果没有重复，说明用户第一次把该商品加入购物车，直接想购物车表中添加一条新数据。
     */
    @RequestMapping(value = "/addCart", method = RequestMethod.POST)
    public CommonResponse addCart(@RequestBody XCart cart, HttpServletRequest request) {
        XItem xItem = new XItem().setId(cart.getItemId()).selectById();
        if (CommonUtils.isEmpty(xItem)){
            return CommonResponse.fail("商品不存在");
        }
        cart.setUid(getUid(request));
        XCart one = cartService.getOne(new QueryWrapper<XCart>()
                .eq("uid", getUid(request)).eq("color", cart.getColor())
                .eq("sort", cart.getSort()).eq("item_id", cart.getItemId()));
        if (CommonUtils.isNotEmpty(one)) {
            one.setCount(cart.getCount() + one.getCount()).updateById();
        } else {
            cart.insert();
        }
        return CommonResponse.success();
    }

    /**
         * 2.55 获取用户购物车列表
     * url：/getCartList
     */
    @RequestMapping(value = "/getCartList", method = RequestMethod.GET)
    public CommonResponse getCartList(HttpServletRequest request) {
        List<CartVo> list = totalService.getCartList(getUid(request));
        return CommonResponse.success(list);
    }

    /**
     * 2.6 修改购物车商品数量
     * url: /modifyCart POST
     */
    @RequestMapping(value = "/modifyCart", method = RequestMethod.POST)
    public CommonResponse modifyCart(@RequestBody XCart cart,HttpServletRequest request) {
        boolean b = cart.updateById();
        if (!b)
            return CommonResponse.fail(CommonStatus.FAULT);
        return CommonResponse.success();
    }

    /**
     * 2.7 删除购物车商品
     * url: /deleteCart GET
     */
    @RequestMapping(value = "/deleteCart", method = RequestMethod.GET)
    public CommonResponse deleteCart(XCart cart,HttpServletRequest request) {
        cart.deleteById();
        return CommonResponse.success();
    }

    /**
     * 2.75 评论商品
     * url: /commentItem POST
     * 注意：将商品状态由3（已确认收货）改为7（已评价）
     */
    @RequestMapping(value = "/commentItem", method = RequestMethod.POST)
    public CommonResponse commentItem(@RequestBody XEvaluate evaluate, HttpServletRequest request) {
        Integer orderId =evaluate.getId();
        XItem xItem = new XItem().setId(evaluate.getItemId()).selectById();
        if (CommonUtils.isEmpty(xItem)){
            return CommonResponse.fail("商品不存在");
        }
        boolean insert = evaluate.setUid(getUid(request)).insert();
        if (!insert)
            return CommonResponse.fail(CommonStatus.FAULT);
        new XOrder().setId(orderId).setStatus(7).updateById();
        return CommonResponse.success();
    }

    /**
     * 2.8 修改用户信息
     * url：/modifyUserInfo POST
     */
    @RequestMapping(value = "/modifyUserInfo", method = RequestMethod.POST)
    public CommonResponse modifyUserInfo(@RequestBody XUser user, HttpServletRequest request) {
        boolean insert = user.setId(getUid(request)).updateById();
        if (!insert)
            return CommonResponse.fail(CommonStatus.FAULT);
        return CommonResponse.success();
    }

    /**
     * 2.9 修改密码
     * url：/modifyPwd POST
     */
    @RequestMapping(value = "/modifyPwd", method = RequestMethod.POST)
    public CommonResponse modifyPwd(@RequestBody Map<String,String> map, HttpServletRequest request) {
        XUser user = userService.getOne(new QueryWrapper<XUser>().eq("id", getUid(request)));
        String oldPwd = map.get("oldPwd");
        String newPwd = map.get("newPwd");
        if (!Md5Utils.encryptPassword(oldPwd, SALT, PASS_COUNT).equals(user.getPassword())){
            return CommonResponse.fail("原始密码不正确");
        }
        boolean b = user.setPassword(Md5Utils.encryptPassword(newPwd, SALT, PASS_COUNT)).updateById();
        if (!b)
            return CommonResponse.fail(CommonStatus.FAULT);
        return CommonResponse.success();
    }

    /**
     * 3.1 发布商品
     * url：/publishItem POST
     * 注意：插入一条新数据需要把saleNum初始化为0
     */
    @RequestMapping(value = "/publishItem", method = RequestMethod.POST)
    public CommonResponse publishItem(@RequestBody PublishItem publishItem, HttpServletRequest request) {
        XItem xItem = new XItem();
        BeanUtils.copyProperties(publishItem,xItem);
        xItem.setColor(publishItem.getColorStr());
        xItem.setSort(publishItem.getSortStr());
        xItem.setSaleNum(0);
        boolean insert = xItem.insert();
        if (!insert)
            return CommonResponse.fail(CommonStatus.FAULT);
        return CommonResponse.success();
    }

    /**
     * 3.2 删除订单
     * url：/deleteOrder?id=1 GET
     */
    @RequestMapping(value = "/deleteOrder", method = RequestMethod.GET)
    public CommonResponse deleteOrder(XOrder order, HttpServletRequest request) {
        order.deleteById();
        return CommonResponse.success();
    }

    /**
     * 3.3 订单发货
     * url: /publishOrder POST
     * 注意：将订单状态由1改为2
     */
    @RequestMapping(value = "/publishOrder", method = RequestMethod.POST)
    public CommonResponse publishOrder(@RequestBody XOrder order, HttpServletRequest request) {
        order.setStatus(2);
        boolean b = order.updateById();
        if (!b)
            return CommonResponse.fail(CommonStatus.FAULT);
        return CommonResponse.success();
    }

    /**
     * 3.4 处理退货
     * url: /manageBackOrder POST
     */
    @RequestMapping(value = "/manageBackOrder", method = RequestMethod.POST)
    public CommonResponse manageBackOrder(@RequestBody XOrder order, HttpServletRequest request) {
        boolean b = order.updateById();
        if (!b)
            return CommonResponse.fail(CommonStatus.FAULT);
        if (order.getStatus()==5){
            XUser user = new XUser().setId(getUid(request)).selectById();
            XOrder xOrder = new XOrder().setId(order.getId()).selectById();
            BigDecimal money = user.getMoney().add(xOrder.getPrice());
            boolean b1 = user.setMoney(money).updateById();
        }
        return CommonResponse.success();
    }

    /**
     * 3.5 修改商品信息
     * url: /modifyItem
     */
    @RequestMapping(value = "/modifyItem", method = RequestMethod.POST)
    public CommonResponse modifyItem(@RequestBody PublishItem publishItem, HttpServletRequest request) {
        XItem xItem = new XItem();
        BeanUtils.copyProperties(publishItem,xItem);
        xItem.setColor(publishItem.getColorStr());
        xItem.setSort(publishItem.getSortStr());
        boolean b = xItem.updateById();
        if (!b)
            return CommonResponse.fail(CommonStatus.FAULT);
        return CommonResponse.success();
    }

    /**
     * 3.6 获取用户列表
     * url：/getUserList GET
     * 注意：获取的用户身份为user
     */
    @RequestMapping(value = "/getUserList", method = RequestMethod.GET)
    public CommonResponse getUserList(HttpServletRequest request) {
        List<XUser> list = userService.list(new QueryWrapper<XUser>().eq("identity", "user"));
        List<Map<String, Object>> mapList = list.stream()
                .map(x -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", x.getId());
                    m.put("account", x.getAccount());
                    m.put("money", x.getMoney());
                    return m;
                }).collect(Collectors.toList());
        return CommonResponse.success(mapList);
    }

    /**
     * 3.7 删除用户
     * url: /deleteUser GET
     */
    @RequestMapping(value = "/deleteUser", method = RequestMethod.GET)
    public CommonResponse deleteUser(XUser user,HttpServletRequest request) {
        user.deleteById();
        return CommonResponse.success();
    }

    /**
     * 3.8 获取统计数据
     * url: /getSaleData GET
     */
    @RequestMapping(value = "/getSaleData", method = RequestMethod.GET)
    public CommonResponse getSaleData(Long time1,Long time2,HttpServletRequest request) {
        List<XItem> time = itemService.list(new QueryWrapper<XItem>().between("time", time1, time2));
        Integer num = 0;
        BigDecimal bigDecimal = new BigDecimal(0);
        for (XItem item:time){
            num+=item.getSaleNum();
            bigDecimal = bigDecimal.add(item.getPrice().multiply(new BigDecimal(item.getSaleNum())));
        }
        map.clear();
        map.put("sale",num);
        map.put("money",bigDecimal);
        return CommonResponse.success(map);
    }
}
