package vip.bzsy.model;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import vip.bzsy.controller.BaseController;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lyf
 * @create 2019-03-30 18:52
 */
@Slf4j
public class JobEnd extends BaseController implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info(format.format(new Date())+"进入JobEnd");
        JobDataMap jobDataMap = context.getTrigger().getJobDataMap();
        Integer userId = (Integer)jobDataMap.get("userId");
        Integer orderId = (Integer)jobDataMap.get("orderId");
        Order order = new Order().setId(orderId).selectById();
        //结束订单
        order.setStatus(5).updateById();
        new Carset().setId(order.getSpaceName()).setStatus(0).updateById();
        //销毁本次任务
        //删除任务
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(orderId + "B", userId + "");
            sched.pauseTrigger(triggerKey);// 停止触发器
            sched.unscheduleJob(triggerKey);// 移除触发器
            sched.deleteJob(JobKey.jobKey(orderId + "B", userId + ""));// 删除任务
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
