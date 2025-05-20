package com.shelly.utils;

import com.shelly.common.ServiceException;
import com.shelly.constants.ScheduleConstant;
import com.shelly.entity.pojo.Task;
import com.shelly.enums.TaskStatusEnum;
import com.shelly.quartz.execution.QuartzDisallowConcurrentExecution;
import com.shelly.quartz.execution.QuartzJobExecution;
import org.quartz.*;

import static com.shelly.constants.CommonConstant.TRUE;

public class ScheduleUtils {
    private ScheduleUtils(){}

    /**
     * 得到quartz任务类
     *
     * @param task 执行计划
     * @return 具体执行任务类
     */
    private static Class<? extends Job> getQuartzJobClass(Task task) {
        // 简单的策略模式
        boolean isConcurrent = TRUE.equals(task.getConcurrent());
        return isConcurrent ? QuartzJobExecution.class : QuartzDisallowConcurrentExecution.class;
    }

    /**
     * 构建任务触发对象
     */
    public static TriggerKey getTriggerKey(Integer taskId, String taskGroup) {
        return TriggerKey.triggerKey(ScheduleConstant.TASK_CLASS_NAME + taskId, taskGroup);
    }

    /**
     * 构建任务键对象
     */
    public static JobKey getJobKey(Integer taskId, String taskGroup) {
        return JobKey.jobKey(ScheduleConstant.TASK_CLASS_NAME + taskId, taskGroup);
    }

    /**
     * 创建定时任务
     */
    public static void createScheduleJob(Scheduler scheduler, Task task) {
        try {
            Class<? extends Job> jobClass = getQuartzJobClass(task);
            // 构建task信息
            Integer taskId = task.getId();
            String taskGroup = task.getTaskGroup();
            JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(getJobKey(taskId, taskGroup)).build();
            // 表达式调度构建器
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(task.getCronExpression());
            cronScheduleBuilder = handleCronScheduleMisfirePolicy(task, cronScheduleBuilder);
            // 按新的cronExpression表达式构建一个新的trigger
            CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(taskId, taskGroup))
                    .withSchedule(cronScheduleBuilder).build();
            // 放入参数，运行时的方法可以获取
            jobDetail.getJobDataMap().put(ScheduleConstant.TASK_PROPERTIES, task);
            // 判断是否存在
            if (scheduler.checkExists(getJobKey(taskId, taskGroup))) {
                // 防止创建时存在数据问题 先移除，然后在执行创建操作
                scheduler.deleteJob(getJobKey(taskId, taskGroup));
            }
            scheduler.scheduleJob(jobDetail, trigger);
            // 暂停任务
            if (task.getStatus().equals(TaskStatusEnum.PAUSE.getStatus())) {
                scheduler.pauseJob(ScheduleUtils.getJobKey(taskId, taskGroup));
            }
        } catch (ServiceException | SchedulerException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * 设置定时任务策略
     */
    public static CronScheduleBuilder handleCronScheduleMisfirePolicy(Task task, CronScheduleBuilder cb) throws ServiceException {
        return switch (task.getMisfirePolicy()) {
            case ScheduleConstant.MISFIRE_DEFAULT -> cb;
            case ScheduleConstant.MISFIRE_IGNORE_MISFIRES -> cb.withMisfireHandlingInstructionIgnoreMisfires();
            case ScheduleConstant.MISFIRE_FIRE_AND_PROCEED -> cb.withMisfireHandlingInstructionFireAndProceed();
            case ScheduleConstant.MISFIRE_DO_NOTHING -> cb.withMisfireHandlingInstructionDoNothing();
            default -> throw new ServiceException("The task misfire policy '" + task.getMisfirePolicy()
                    + "' cannot be used in cron schedule tasks");
        };
    }
}
