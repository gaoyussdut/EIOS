//package top.toptimus.controller;
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import top.toptimus.common.result.Result;
//import top.toptimus.constantConfig.Constants;
//import top.toptimus.meta.signGroup.CountersignSubmitDTO;
//import top.toptimus.service.domainService.PlaceService;
//import top.toptimus.tokendata.TokenDataDto;
//
//@Api(value = "汇签接口", tags = "汇签操作")
//@RestController
//@RequestMapping(value = "/countersign")
//@Controller
//public class CountersignController {
//    @Autowired
//    private PlaceService placeService;
//    /*
//    汇签 start
//     */
////    /**
////     * 创建汇签任务
////     */
////    @ApiOperation(value = "创建汇签任务单据")
////    @PutMapping(value = "/countersignTask")
////    public Result createCountersignTask() {
////        return placeService.createBill(Constants.countersign_task_meta_id);
////    }
//
//    /**
//     * 提交汇签任务单据
//     *
//     * @param metaId        单据metaId
//     * @param tokenId       单据tokenId
//     * @param tokenDataDto  汇签任务tokendata
//     * @return  Result
//     */
//    @ApiOperation(value = "提交汇签任务单据")
//    @PostMapping(value = "/countersignTask")
//    public Result submitCountersignTask(
//            @RequestParam String metaId
//            , @RequestParam String tokenId
//            , @RequestBody TokenDataDto tokenDataDto
//    ) {
//        return placeService.submitCountersignTaskToken(tokenDataDto,Constants.countersign_task_meta_id,metaId,tokenId);//TODO
//    }
//
//
////    /**
////     * 启动汇签
////     */
////    @ApiOperation(value = "创建汇签表单")
////    @PutMapping(value = "/countersign")
////    public Result createCountersign() {
////        return placeService.createBill(Constants.countersign_bill_meta_id);
////    }
//
//
//    /**
//     * 提交汇签意见
//     *
//     * @param countersignSubmitDTO  汇签信息提交DTO
//     * @return Result
//     */
//    @ApiOperation(value = "提交汇签表单")
//    @PostMapping(value = "/countersign")
//    public Result submitCountersignToken(
//            @RequestBody CountersignSubmitDTO countersignSubmitDTO
//    ) {
//        return placeService.submitCountersignToken(countersignSubmitDTO,Constants.countersign_task_meta_id,Constants.countersign_bill_meta_id);
//    }
//
//    /**
//     * 查询所有汇签意见
//     *
//     * @param metaId    单据metaId
//     * @param tokenId   单据tokenId
//     * @return Result
//     */
//    @ApiOperation(value = "查询汇签意见")
//    @GetMapping(value = "/countersign")
//    public Result getCountersign(
//            @RequestParam String metaId
//            , @RequestParam String tokenId
//
//    ) {
//        return placeService.getCountersigns(metaId, tokenId,Constants.countersign_bill_meta_id);
//    }
//
//
//    /**
//     * 添加参与者
//     *
//     * @param countersignTokenId 汇签意见tokenId
//     * @return Result
//     */
//    @ApiOperation(value = "添加参与者")
//    @PutMapping(value = "/participatar/countersign-token-id")
//    public Result createParticipatar(
//            @RequestParam String countersignTokenId
//    ) {
//        return placeService.createEntryToken(Constants.countersign_entry_meta_id, countersignTokenId);
//    }
//
//    /**
//     * 提交参与者
//     *
//     * @param countersignTokenId  汇签意见tokenId
//     * @param tokenDataDto 参与者token
//     * @return result
//     */
//    @ApiOperation(value = "提交参与者")
//    @PostMapping(value = "/participatar/countersign-token-id")
//    public Result submitParticipatar(
//            @RequestParam String countersignTokenId
//            , @RequestBody TokenDataDto tokenDataDto
//    ) {
//        return placeService.saveEntryToken(countersignTokenId, tokenDataDto, Constants.countersign_entry_meta_id);
//    }
//
//    /**
//     * 查询参与者
//     *
//     * @param countersignTokenId  汇签意见tokenId
//     * @return Result
//     */
//    @ApiOperation(value = "查询参与者")
//    @GetMapping(value = "/participatar/countersign-token-id")
//    public Result getParticipatarTokens(
//            @RequestParam String countersignTokenId
//    ) {
//        return placeService.getEntryToken(Constants.countersign_bill_meta_id, countersignTokenId, Constants.countersign_entry_meta_id);
//    }
//    /*
//    汇签 end
//     */
//
//}
