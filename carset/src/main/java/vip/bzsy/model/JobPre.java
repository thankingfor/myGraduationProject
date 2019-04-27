package vip.bzsy.model;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import vip.bzsy.controller.BaseController;

import java.util.Date;

/**
 * @author lyf
 * @create 2019-03-30 18:16
 */
@Slf4j
public class JobPre extends BaseController implements Job{

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        log.info(format.format(new Date())+"进入JobPre");
        JobDataMap jobDataMap = context.getTrigger().getJobDataMap();
        Integer userId = (Integer)jobDataMap.get("userId");
        Integer orderId = (Integer)jobDataMap.get("orderId");
        Order order = new Order().setId(orderId).selectById();

        try {
            //更嗨order的状态
            order.setStatus(2).updateById();
            //更变catset表
            new Carset().setId(order.getSpaceName()).setStatus(1).updateById();
            //创建新的任务
            Date startDate = new Date();
            startDate.setTime(order.getStartTime()+order.getTimeLength()+60*60*1000L);
            JobDetail jobDetail = JobBuilder.newJob(JobEnd.class)
                    .withIdentity(orderId+"B",order.getUid()+"")
                    .build();
            Trigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(orderId+"B",order.getUid()+"")
                    .usingJobData("userId",userId)
                    .usingJobData("orderId",orderId)
                    .startAt(startDate)
                    .build();
            Scheduler scheduler = schedulerFactory.getScheduler();
            scheduler.scheduleJob(jobDetail,trigger);
            scheduler.start();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

        //删除任务
        try {
            Scheduler sched = schedulerFactory.getScheduler();
            TriggerKey triggerKey = TriggerKey.triggerKey(orderId + "A", userId + "");
            sched.pauseTrigger(triggerKey);// 停止触发器
            sched.unscheduleJob(triggerKey);// 移除触发器
            sched.deleteJob(JobKey.jobKey(orderId + "A", userId + ""));// 删除任务
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
