package com.shelly.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaMode;
import com.shelly.annotation.OptLogger;
import com.shelly.common.Result;
import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.TaskQuery;
import com.shelly.entity.vo.req.StatusReq;
import com.shelly.entity.vo.req.TaskReq;
import com.shelly.entity.vo.req.TaskRunReq;
import com.shelly.entity.vo.res.TaskBackResp;
import com.shelly.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.shelly.constants.OptTypeConstant.*;

/**
 * 定时任务控制器
 * @author shelly
 */
@Tag(name = "定时任务模块")
@RestController
//checked
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Operation(summary = "查看定时任务列表")
    @SaCheckPermission("monitor:task:list")
    @GetMapping("/admin/task/list")
    public Result<PageResult<TaskBackResp>> listTaskBackVO(TaskQuery taskQuery) {
        return Result.success(taskService.listTaskBackVO(taskQuery));
    }

    @Operation(summary = "添加定时任务")
    @OptLogger(value = ADD)
    @SaCheckPermission("monitor:task:add")
    @PostMapping("/admin/task/add")
    public Result<?> addTask(@Validated @RequestBody TaskReq task) {
        taskService.addTask(task);
        return Result.success();
    }
    @Operation(summary = "修改定时任务")
    @OptLogger(value = UPDATE)
    @SaCheckPermission(value = "monitor:task:update")
    @PutMapping("/admin/task/update")
    public Result<?> updateTask(@Validated @RequestBody TaskReq task) {
        taskService.updateTask(task);
        return Result.success();
    }

    @Operation(summary = "删除定时任务")
    @OptLogger(value = DELETE)
    @SaCheckPermission("monitor:task:delete")
    @DeleteMapping("/admin/task/delete")
    public Result<?> deleteTask(@RequestBody List<Integer> taskIdList) {
        taskService.deleteTask(taskIdList);
        return Result.success();
    }

    @Operation(summary = "修改定时任务状态")
    @OptLogger(value = UPDATE)
    @SaCheckPermission(value = {"monitor:task:update", "monitor:task:status"}, mode = SaMode.OR)
    @PutMapping("/admin/task/changeStatus")
    public Result<?> updateTaskStatus(@Validated @RequestBody StatusReq taskStatus) {
        taskService.updateTaskStatus(taskStatus);
        return Result.success();
    }
    @Operation(summary = "执行定时任务")
    @SaCheckPermission("monitor:task:run")
    @PutMapping("/admin/task/run")
    @OptLogger(value = UPDATE)
    public Result<?> runTask(@RequestBody TaskRunReq taskRun) {
        taskService.runTask(taskRun);
        return Result.success();
    }

}
