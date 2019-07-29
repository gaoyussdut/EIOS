package top.toptimus.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.toptimus.common.enums.taskEnum.TaskTypeEnum;
import top.toptimus.common.enums.taskEnum.action.TaskActionEnum;
import top.toptimus.common.result.Result;
import top.toptimus.service.TaskService;
import top.toptimus.transformationService.TransformationService;

/**
 * 任务流接口
 *
 * @author lizongsheng
 */
@Api(value = "任务流接口", tags = "任务流管理")
@RestController
@RequestMapping(value = "/task")
@Controller
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private TransformationService transformationService;

    /**
     * 获取表单全部任务
     *
     * @param billMetaId  表头meta
     * @param billTokenId 表头tokenId
     */
    @GetMapping(value = "/tasks/meta-id/token-id")
    public Result getAllTasks(@RequestParam String billMetaId, @RequestParam String billTokenId) {
        return taskService.getAllTasks(billMetaId, billTokenId);
    }

    /**
     * 获取当前任务
     *
     * @param billMetaId  表头meta
     * @param billTokenId 表头tokenId
     */
    @GetMapping(value = "/currentTask/meta-id/token-id")
    public Result getCurrentTask(@RequestParam String billMetaId, @RequestParam String billTokenId) {
        return taskService.getCurrentTask(billMetaId, billTokenId);
    }

    /**
     * 立项
     *
     * @param billMetaId  表头meta
     * @param billTokenId 表头tokenId
     */
    @GetMapping(value = "/setUpProject/meta-id/token-id/target-meta-id")
    public Result setUpProject(@RequestParam String billMetaId, @RequestParam String billTokenId, @RequestParam String targetMetaId) {
//        return transformationService.transformation(billMetaId, billTokenId, targetMetaId);
        return null;
    }

    /**
     * 修改当前任务状态
     *
     * @param billMetaId  表头meta
     * @param billTokenId 表头tokenId
     */
    @GetMapping(value = "/updateTaskStatus/meta-id/token-id/task-action-enum")
    public Result setUpProject(@RequestParam String billMetaId, @RequestParam String billTokenId, @RequestParam TaskActionEnum taskActionEnum) {
        return taskService.updateTaskStatus(billMetaId, billTokenId, taskActionEnum);
    }

    /**
     * 根据任务类型获取所有任务
     *
     * @param billMetaId   表头meta
     * @param billTokenId  表头tokenId
     * @param taskTypeEnum 任务类型
     */
    @GetMapping(value = "/task/meta-id/token-id/task-type-enum")
    public Result getTaskByTaskType(@RequestParam String billMetaId, @RequestParam String billTokenId, @RequestParam TaskTypeEnum taskTypeEnum) {
        return taskService.getTaskByTaskType(billMetaId, billTokenId, taskTypeEnum);
    }
}
