package com.shelly.quartz.execution;

import com.shelly.entity.pojo.Task;
import com.shelly.utils.TaskInvokeUtils;
import org.quartz.JobExecutionContext;

public class QuartzJobExecution extends AbstractQuartzJob {
    @Override
    protected void doExecute(JobExecutionContext context, Task task) throws Exception {
        TaskInvokeUtils.invokeMethod(task);
    }
}
