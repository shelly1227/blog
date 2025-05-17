package com.shelly.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.shelly.common.Result;
import com.shelly.entity.pojo.ExceptionLog;
import com.shelly.entity.pojo.VisitLog;
import com.shelly.entity.vo.Response.OperationLogResp;
import com.shelly.entity.vo.Response.TaskLogResp;

import com.shelly.entity.vo.PageResult;
import com.shelly.entity.vo.Query.LogQuery;
import com.shelly.entity.vo.Query.TaskQuery;
import com.shelly.service.ExceptionLogService;
import com.shelly.service.OperationLogService;
import com.shelly.service.TaskLogService;
import com.shelly.service.VisitLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LogsController {
    private final ExceptionLogService exceptionLogService;
    private final OperationLogService operationLogService;
    private final TaskLogService taskLogService;
    private final VisitLogService visitLogService;
    @DeleteMapping("/admin/exception/delete")
    @SaCheckPermission("log:exception:delete")
    public Result<?> deleteAllExceptions(@RequestBody  List<Integer>logIdList) {
        exceptionLogService.removeByIds(logIdList);
        return Result.success();
    }

    @GetMapping("/admin/exception/list")
    @SaCheckPermission("log:exception:list")
    public Result<PageResult<ExceptionLog>> getAllExceptions(LogQuery logQuery) {
        return Result.success(exceptionLogService.listExceptionLogs(logQuery));
    }
    @DeleteMapping("/admin/operation/delete")
    @SaCheckPermission("log:operation:delete")
    public Result<?> deleteAllOperations(@RequestBody  List<Integer>logIdList) {
        operationLogService.removeByIds(logIdList);
        return Result.success();
    }
    @GetMapping("/admin/operation/list")
    @SaCheckPermission("log:operation:list")
    public Result<PageResult<OperationLogResp>> getAllOperations(LogQuery logQuery) {
        return Result.success(operationLogService.listExceptionLogs(logQuery));
    }
    @DeleteMapping("/admin/taskLog/clear")
    @SaCheckPermission("log:task:clear")
    public Result<?> clearTaskLog() {
        taskLogService.clearTaskLog();
        return Result.success();
    }
    @DeleteMapping("/admin/taskLog/delete")
    @SaCheckPermission("log:task:delete")
    public Result<?> deleteAllTaskLogs(@RequestBody  List<Integer>logIdList) {
        taskLogService.removeByIds(logIdList);
        return Result.success();
    }
    @GetMapping("/admin/taskLog/list")
    @SaCheckPermission("log:task:list")
    public Result<PageResult<TaskLogResp>> getAllTaskLogs(TaskQuery taskQuery) {
        return Result.success(taskLogService.listLogs(taskQuery));
    }
    @DeleteMapping("/admin/visit/delete")
    @SaCheckPermission("log:visit:delete")
    public Result<?> deleteAllVisits(@RequestBody  List<Integer>logIdList) {
        visitLogService.removeByIds(logIdList);
        return Result.success();
    }
    @GetMapping("/admin/visit/list")
    @SaCheckPermission("log:visit:list")
    public Result<PageResult<VisitLog>> getAllVisits(LogQuery logQuery) {
        return Result.success(visitLogService.listVisitLogIds(logQuery));
    }
}
