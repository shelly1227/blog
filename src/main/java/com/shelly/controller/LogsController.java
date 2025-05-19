package com.shelly.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shelly.common.Result;
import com.shelly.entity.pojo.ExceptionLog;
import com.shelly.entity.pojo.VisitLog;
import com.shelly.entity.vo.res.OperationLogResp;
import com.shelly.entity.vo.res.TaskLogResp;

import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.query.LogQuery;
import com.shelly.entity.vo.query.TaskQuery;
import com.shelly.service.ExceptionLogService;
import com.shelly.service.OperationLogService;
import com.shelly.service.TaskLogService;
import com.shelly.service.VisitLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "日志模块")
@RequiredArgsConstructor
//checked
public class LogsController {
    private final ExceptionLogService exceptionLogService;
    private final OperationLogService operationLogService;
    private final TaskLogService taskLogService;
    private final VisitLogService visitLogService;
    @DeleteMapping("/admin/exception/delete")
    @SaCheckPermission("log:exception:delete")
    @Operation(summary = "删除所有异常日志")
    public Result<?> deleteAllExceptions(@RequestBody  List<Integer>logIdList) {
        exceptionLogService.removeByIds(logIdList);
        return Result.success();
    }

    @GetMapping("/admin/exception/list")
    @Operation(summary = "获取所有异常日志")
    @SaCheckPermission("log:exception:list")
    public Result<PageResult<ExceptionLog>> getAllExceptions(LogQuery logQuery) {
        return Result.success(exceptionLogService.listExceptionLogs(logQuery));
    }
    @DeleteMapping("/admin/operation/delete")
    @Operation(summary = "删除所有操作日志")
    @SaCheckPermission("log:operation:delete")
    public Result<?> deleteAllOperations(@RequestBody  List<Integer>logIdList) {
        operationLogService.removeByIds(logIdList);
        return Result.success();
    }
    @GetMapping("/admin/operation/list")
    @Operation(summary = "获取所有操作日志")
    @SaCheckPermission("log:operation:list")
    public Result<PageResult<OperationLogResp>> getAllOperations(LogQuery logQuery) {
        return Result.success(operationLogService.listExceptionLogs(logQuery));
    }
    @DeleteMapping("/admin/taskLog/clear")
    @SaCheckPermission("log:task:clear")
    @Operation(summary = "清空定时任务日志")
    public Result<?> clearTaskLog() {
        taskLogService.clearTaskLog();
        return Result.success();
    }
    @DeleteMapping("/admin/taskLog/delete")
    @Operation(summary = "删除所有定时任务日志")
    @SaCheckPermission("log:task:delete")
    public Result<?> deleteAllTaskLogs(@RequestBody  List<Integer>logIdList) {
        taskLogService.removeByIds(logIdList);
        return Result.success();
    }
    @GetMapping("/admin/taskLog/list")
    @Operation(summary = "获取所有定时任务日志")
    @SaCheckPermission("log:task:list")
    public Result<PageResult<TaskLogResp>> getAllTaskLogs(TaskQuery taskQuery) {
        return Result.success(taskLogService.listLogs(taskQuery));
    }
    @DeleteMapping("/admin/visit/delete")
    @Operation(summary = "删除所有访问日志")
    @SaCheckPermission("log:visit:delete")
    public Result<?> deleteAllVisits(@RequestBody  List<Integer>logIdList) {
        visitLogService.removeByIds(logIdList);
        return Result.success();
    }
    @GetMapping("/admin/visit/list")
    @SaCheckPermission("log:visit:list")
    @Operation(summary = "获取所有访问日志")
    public Result<PageResult<VisitLog>> getAllVisits(LogQuery logQuery) {
        return Result.success(visitLogService.listVisitLogIds(logQuery));
    }
}
