package top.toptimus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import top.toptimus.common.enums.process.VertexTypeEnum;
import top.toptimus.common.result.Result;
import top.toptimus.exception.TopErrorCode;
import top.toptimus.place.PlaceDTO;
import top.toptimus.resultModel.ResultErrorModel;
import top.toptimus.service.domainService.ProcessDefinitionService;
import top.toptimus.service.domainService.ProcessInsService;
import java.util.Map;


/**
 * 把各实体类封装成服务，以便前端统一调用
 *
 */
@Api(value = "系统用户接口", tags = "系统管理")
@RestController
@RequestMapping(value = "/process")
@Controller
public class ProcessController {

    private static Logger logger = LoggerFactory.getLogger(ProcessController.class);

    @Autowired
    private ProcessInsService processInsService;
    @Autowired
    private ProcessDefinitionService processDefinitionService;

    /**
     * bpmnXml存储以及关系建立
     *
     * @param bpmnContent       bpmn文件
     * @param userTaskIdAndMetaId Map<String, String> userTaskIdAndMetaId   K:节点id  V:metaId
     * @return Result
     */
    @ApiOperation(value = "bpmnXml存储以及关系建立", notes = "K:userTaskId  V:metaId")
    @PostMapping(value = "/bpmnXmlUpload")
    public Result bpmnXmlUpload(@RequestParam String bpmnContent , @RequestBody Map<String, String> userTaskIdAndMetaId
    ) {
        if (StringUtils.isEmpty(bpmnContent)) {
            return new ResultErrorModel(TopErrorCode.NULL_OBJ).getResult();
        }
        logger.info("bpmnContent：" + bpmnContent);
        return processDefinitionService.bpmnXmlUpload(bpmnContent, userTaskIdAndMetaId);

    }

    /**
     * 根据processId查找BPMN的xml
     *
     * @param processId 流程id
     * @return BPMN xml
     */
    @ApiOperation(value = "根据processId取得BPMN的XML")
    @GetMapping(value = "/getBPMNXmlString")
    public String getBPMNXmlString(@RequestParam String processId) {
        return processDefinitionService.getBPMNXmlString(processId);
    }

    /**
     * 实例BPMN流程
     *
     * @param processId      流程id
     * @param billTokenId    tokenId
     * @return Result 实例化流程
     */
    @ApiOperation(value = "实例BPMN流程")
    @RequestMapping(value = {"/generateProcessIns"}, method = RequestMethod.GET, produces = "application/json")
    public Result generateProcessIns(@RequestParam String processId, @RequestParam(required = false) String billTokenId) {
        // 查找头节点
        return processInsService.insertProcessIns(processId, billTokenId);
    }

    /**
     * 实例单据下的审批流程
     *
     * @param processId  流程id
     * @param tokenId    tokenId
     * @return Result实例化流程
     */
    @ApiOperation(value = "实例单据下的审批流程")
    @RequestMapping(value = {"/generateBillProcessIns"}, method = RequestMethod.GET, produces = "application/json")
    public Result generateBillProcessIns(@RequestParam String metaId,@RequestParam String tokenId ,@RequestParam String taskId ,@RequestParam String processId) {
        // 查找头节点
        return processInsService.insertProcessIns(metaId, tokenId, taskId, processId);
    }

    /**
     * 根据单据metaId和tokenId查询流程图及审批状态
     *
     * @param metaId   单据metaId
     * @param tokenId  单据tokenId
     * @return Result 流程图及审批状态
     */
    @ApiOperation(value = "实例单据下的审批流程")
    @RequestMapping(value = {"/findProcessByMetaAndTokenId"}, method = RequestMethod.GET, produces = "application/json")
    public Result findProcessByMetaAndTokenId(String metaId ,String tokenId) {
        return processInsService.findProcessByMetaAndTokenId(metaId ,tokenId);
    }

    /**
     * 根据流程ID取得指定节点信息
     *
     * @param processId 流程Id
     * @param type      STARTEVENT, USERTASK, ENDEVENT
     * @return user task 一览
     */
    @ApiOperation(value = "根据流程ID取得指定节点信息")
    @RequestMapping(value = {"/getUserTaskInfo"}, method = RequestMethod.GET, produces = "application/json")
    public Result getUserTaskInfo(@RequestParam String processId, @RequestParam String type) {
        return processDefinitionService.findByProcessIdAndType(processId, VertexTypeEnum.valueOf(type));
    }

    /**
     * 根据UserTaskId和状态取一览数据
     *
     * @param userTaskId      节点信息
     * @param userTaskStatus  节点状态
     * @return Result
     */
    @ApiOperation(value = "根据节点Id和状态取得列表")
    @RequestMapping(value = {"/getUserTasklist"}, method = RequestMethod.GET, produces = "application/json")
    public Result getUserTaskList(String userTaskId , String userTaskStatus ) {
        return processInsService.findUserTaskList(userTaskId, userTaskStatus);
    }

    /**
     * 取得所有processId和Name
     *
     * @return Result(k ： process id v : process name)
     */
    @ApiOperation(value = "取得所有processId和Name")
    @GetMapping(value = "/findAllProcess")
    public Result findAllProcess() {
        return processDefinitionService.findAllProcess();
    }

    /**
     * 待办详细:业务信息（data）
     *
     * @param tokenId           token id
     * @return Result(PlaceDTO) 库所
     */
    @ApiOperation(value = "节点待办详细:业务信息")
    @RequestMapping(value = {"/userTaskDetail"}, method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Result getUserTaskProcessDetail(@RequestParam String tokenId, @RequestParam String userTaskId) {
        return processDefinitionService.getProcessUserTaskDTO(tokenId, userTaskId);
    }

    /**
     * 待办详细:表单提交
     *
     * @param placeDTO 表单库所
     * @return result  保存结果
     */
    @ApiOperation(value = "表单详细:保存提交")
    @PostMapping(value = "/taskSubmit")
    public Result submitTaskDatas(@RequestBody PlaceDTO placeDTO) {
        processInsService.transictionUserTaskInProcess(placeDTO);
        return Result.success();
    }

}