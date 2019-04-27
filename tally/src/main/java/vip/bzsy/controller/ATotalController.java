package vip.bzsy.controller;

import com.baidu.aip.ocr.AipOcr;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import vip.bzsy.common.CommonResponse;
import vip.bzsy.common.CommonStatus;
import vip.bzsy.common.CommonUtils;
import vip.bzsy.common.DateUtils;
import vip.bzsy.model.*;
import vip.bzsy.model.vo.ExpenditureVo;
import vip.bzsy.model.vo.IncomeVo;
import vip.bzsy.model.vo.SortTypeVo;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

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
     * 2.1 获取支出列表
     * url：/getOutByDay GET
     * /getOutList?time1=1111111111111&time2=1111111111111
     * 如：获取2019年3月29日的支出，time1即为2019年3月29日0点0分，time2时间为2019年3月30日0点0分
     * @return
     */
    @RequestMapping(value = "/getOutList",method = RequestMethod.GET)
    public CommonResponse getOutList(@RequestParam(value = "time1",defaultValue = "-1") Long startTime,
                                      @RequestParam(value = "time2") Long endTime,
                                      HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        Integer userId = (Integer) claims.get("userId");
        User user = new User().setId(userId).selectById();
        if (CommonUtils.isEmpty(user)){
            return CommonResponse.fail(CommonStatus.TOKEN_LOSE);
        }
        if (startTime==-1L){
            startTime = 0L;
            //startTime = user.getRegTime();
        }
        List<Expenditure> expenditureList = expenditureService.list(new QueryWrapper<Expenditure>()
                .eq("uid",userId)
                .between("time", startTime, endTime)
                .orderByDesc("time"));
        Map<Long,List<ExpenditureVo>> vosMap = new LinkedHashMap<>();
        List<List<ExpenditureVo>> vosList = new LinkedList<>();
        for (Expenditure expenditure:expenditureList){
            Long nowStartDateTime = DateUtils.getNowStartDateTime(expenditure.getTime());
            ExpenditureVo vo = new ExpenditureVo();
            BeanUtils.copyProperties(expenditure,vo);
            vo.setSort(new SortType().setId(expenditure.getSortId()).selectById().getSort());
            //判断当前日期是否存在map中 不存在就新建一个list存入map
            if (vosMap.containsKey(nowStartDateTime)){
                List<ExpenditureVo> vos = vosMap.get(nowStartDateTime);
                vos.add(vo);
            } else {
                List<ExpenditureVo> vos = new LinkedList<>();
                vos.add(vo);
                vosMap.put(nowStartDateTime,vos);
                vosList.add(vos);
            }
        }
        return CommonResponse.success(vosList);
    }

    /**
     * 2.2 获取收入列表
     * url：/getInList
     * /getInList?time1=1111111111111&time2=111111111111
     * 与上同理
     * @return
     */
    @RequestMapping(value = "/getInList",method = RequestMethod.GET)
    public CommonResponse getInList(@RequestParam(value = "time1",defaultValue = "-1") Long startTime,
                                    @RequestParam(value = "time2") Long endTime,
                                    HttpServletRequest request){
        log.info(startTime+"");
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        Integer userId = (Integer) claims.get("userId");
        User user = new User().setId(userId).selectById();
        if (CommonUtils.isEmpty(user)){
            return CommonResponse.fail(CommonStatus.TOKEN_LOSE);
        }
        if (startTime==-1L){
            startTime = user.getRegTime();
        }
        List<Income> incomeList = incomeService.list(new QueryWrapper<Income>()
                .eq("uid",userId)
                .between("time", startTime, endTime)
                .orderByDesc("time"));

        Map<Long,List<IncomeVo>> vosMap = new LinkedHashMap<>();
        List<List<IncomeVo>> vosList = new LinkedList<>();
        for (Income income:incomeList){
            Long nowStartDateTime = DateUtils.getNowStartDateTime(income.getTime());
            IncomeVo vo = new IncomeVo();
            BeanUtils.copyProperties(income,vo);
            //判断当前日期是否存在map中 不存在就新建一个list存入map
            if (vosMap.containsKey(nowStartDateTime)){
                List<IncomeVo> vos = vosMap.get(nowStartDateTime);
                vos.add(vo);
            } else {
                List<IncomeVo> vos = new LinkedList<>();
                vos.add(vo);
                vosMap.put(nowStartDateTime,vos);
                vosList.add(vos);
            }
        }
        return CommonResponse.success(vosList);
    }

    /**
     *2.4 添加支出
     * url： /addOut POST
     * @param expenditure
     * @param request
     * @return
     */
    @RequestMapping(value = "/addOut",method = RequestMethod.POST)
    public CommonResponse addOut(@RequestBody Expenditure expenditure,HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        Integer userId = (Integer) claims.get("userId");
        expenditure.setSortId(expenditure.getSort());
        boolean insert = expenditure.setUid(userId).insert();
        if (!insert){
            return CommonResponse.fail("添加支出失败");
        }
        return CommonResponse.success();
    }

    /**
     * 2.5 添加收入
     * url： /addIn POST
     * @param income
     * @param request
     * @return
     */
    @RequestMapping(value = "/addIn",method = RequestMethod.POST)
    public CommonResponse addIn(@RequestBody Income income,HttpServletRequest request){
        log.info(income.toString());
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        Integer userId = (Integer) claims.get("userId");
        boolean insert = income.setUid(userId).insert();
        if (!insert){
            return CommonResponse.fail("添加支出失败");
        }
        return CommonResponse.success();
    }

    /**
     * 2.6 获取消费分类
     * url: /getSort
     * @return
     */
    @RequestMapping(value = "/getSort",method = RequestMethod.GET)
    public CommonResponse getSort(HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        Integer userId = (Integer) claims.get("userId");
        List<SortType> list = sortTypeService.list(new QueryWrapper<SortType>()
                .and(wapper -> wapper.eq("uid",userId).or().eq("status",0)));
        List<SortTypeVo> listsort = new LinkedList<>();
        for (SortType sortType:list){
            SortTypeVo vo = new SortTypeVo(sortType.getId(),sortType.getSort(),sortType.getStatus());
            listsort.add(vo);
        }
        return CommonResponse.success(listsort);
    }

    /**
     * 2.7 修改某项支出
     * url：/alterOut POST
     * @param request
     * @return
     */
    @RequestMapping(value = "/alterOut",method = RequestMethod.POST)
    public CommonResponse alterOut(@RequestBody Expenditure expenditure, HttpServletRequest request){
        boolean b = expenditure.setSortId(expenditure.getSort()).updateById();
        if (!b){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    /**
     * 2.8 修改某项收入
     * url：/alterIn POST
     * @param request
     * @return
     */
    @RequestMapping(value = "/alterIn",method = RequestMethod.POST)
    public CommonResponse alterIn(@RequestBody Income income, HttpServletRequest request){
        boolean b = income.updateById();
        if (!b){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    /**
     * 2.9 删除某项支出
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleteOut",method = RequestMethod.GET)
    public CommonResponse deleteOut(Expenditure expenditure, HttpServletRequest request){
        boolean b = expenditure.deleteById();
        if (!b){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    /**
     * 2.10 删除某项收入
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleteIn",method = RequestMethod.GET)
    public CommonResponse deleteIn(Income income, HttpServletRequest request){
        boolean b = income.deleteById();
        if (!b){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    /**
     * 3.1 添加某用户的私有支出分类
     * url：/addSort GET
     */
    @RequestMapping(value = "/addSort",method = RequestMethod.GET)
    public CommonResponse addSort(SortType type, HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        Integer userId = (Integer) claims.get("userId");
        type.setUid(userId);
        type.setStatus(1);
        boolean b = type.insert();
        if (!b){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    /**
     * 3.2 删除支出分类
     * url：/deleteSort GET
     */
    @RequestMapping(value = "/deleteSort",method = RequestMethod.GET)
    public CommonResponse deleteSort(SortType type, HttpServletRequest request){
        boolean b = type.deleteById();
        if (!b){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }
    /**
     * 3.3 添加还款日
     * url：/addRepayment POST
     */
    @RequestMapping(value = "/addRepayment",method = RequestMethod.POST)
    public CommonResponse addRepayment(@RequestBody Repayment repayment, HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        Integer userId = (Integer) claims.get("userId");
        repayment.setUid(userId);
        boolean b = repayment.insert();
        if (!b){
            return CommonResponse.fail(CommonStatus.FAULT);
        }
        return CommonResponse.success();
    }

    /**
     * 3.4 获取某用户还款日列表
     * url: /getRepayment GET
     */
    @RequestMapping(value = "/getRepayment",method = RequestMethod.GET)
    public CommonResponse getRepayment(HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        Integer userId = (Integer) claims.get("userId");
        List<Repayment> list = repaymentService.list(new QueryWrapper<Repayment>().eq("uid", userId));
        return CommonResponse.success(list);
    }

    /**
     * 3.5 删除某用户还款日
     * url: /deleteRepayment GET
     */
    @RequestMapping(value = "/deleteRepayment",method = RequestMethod.GET)
    public CommonResponse deleteRepayment(Repayment repayment,HttpServletRequest request){
        repayment.deleteById();
        return CommonResponse.success();
    }

    /**
     * 3.1 获取上传截图识别到的信息
     * url: /getPicInfo POST
     * url: 'http://study.esunr.xyz/Fuy_mwVGEgsMO7d2vankdD0otgee' // 图片地址
     */
    @RequestMapping(value = "/getPicInfo",method = RequestMethod.POST)
    public CommonResponse getPicInfo(@RequestBody Map<String,Object> map,HttpServletRequest request){
        String url = (String) map.get("url");
        log.info("myurl"+url);
        AipOcr client = new AipOcr(APP_ID, API_KEY, SECRET_KEY);
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("language_type", "CHN_ENG");
        options.put("detect_direction", "true");
        options.put("detect_language", "true");
        options.put("probability", "true");
        JSONObject jsonObject  = client.basicGeneralUrl(url,options);
        /**
         * 解析json
         */
        List<String> keys = new ArrayList<>();
        Map<String, Object> map1 = jsonObject.toMap();
        List<Map<String,Object>> word_result = (List<Map<String, Object>>) map1.get("words_result");
//        for (Map<String,Object> maps : word_result){
//            Object words = maps.get("words");
//            String word = (String) words.toString();
//            keys.add(word);
//        }
//        /**
//         * 分析字符串
//         */
//        Claims claims = (Claims) request.getAttribute("CLAIMS");
//        Integer userId = (Integer) claims.get("userId");
//        Expenditure expenditure = new Expenditure();
//        expenditure.setWay(4);
//        expenditure.setUid(userId);
//        expenditure.setTime(new Date().getTime());
//        BigDecimal bigDecimal = null;
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
//        try {
//            for (String key : keys){
//                if (key.indexOf(".00")>0){
//                    bigDecimal = new BigDecimal(key);
//                }
//            }
//        }catch (Exception e){
//            return CommonResponse.fail("读取失败");
//        }
//        if (CommonUtils.isEmpty(bigDecimal)){
//            return CommonResponse.fail("读取失败");
//        }
//        map.clear();
//        map.put("money",bigDecimal);
//        map.put("remark",keys.get(new Random().nextInt(keys.size())));
//        map.put("time",expenditure.getTime());
        return CommonResponse.success(word_result);
    }

    /**
     * 3.2 获取用户行为
     * url: /getUserRanking GET
     * @return
     */
    @RequestMapping(value = "/getUserRanking",method = RequestMethod.GET)
    public CommonResponse getUserRanking(HttpServletRequest request){
        Claims claims = (Claims) request.getAttribute("CLAIMS");
        Integer userId = (Integer) claims.get("userId");
        int idCount = expenditureService.count(new QueryWrapper<Expenditure>().eq("uid", userId));
        if(idCount <= 0){
            map.clear();
            map.put("totalCost",0);
            map.put("costRanking",0);
            map.put("mostWay","无");
            map.put("wayRanking","不消费");
            return CommonResponse.success(map);
        }
        BigDecimal totalCost = expenditureService.getTotalCast(userId);
        List<BigDecimal> list = expenditureService.getTotalCastList();
        long count = list.stream().filter(bigDecimal -> totalCost.compareTo(bigDecimal) > 0).count();
        float v = ((float) count / list.size()) * 100;
        int costRanking = (int) v;
        Map<String,Object> way = expenditureService.mostWay(userId);
        List<Integer> listUid = expenditureService.getUidList();
        long count1 = listUid.stream().filter(uid -> {
            Map<String, Object> myway = expenditureService.mostWay(uid);
            log.info(myway.toString()+"uid="+uid);
            if ((Integer) way.get("way") == (Integer) myway.get("way")) {
                if ((long) way.get("num") < (long) myway.get("num"))
                    return false;
            }
            return true;
        }).count();
        log.info(count1+","+listUid.size());
        int wayRanking = (int)((float) count1 / listUid.size()) * 100;

        map.clear();
        map.put("totalCost",totalCost);
        map.put("costRanking",costRanking);
        map.put("mostWay",way.get("way"));
        map.put("wayRanking",wayRanking);
        return CommonResponse.success(map);
    }


}
