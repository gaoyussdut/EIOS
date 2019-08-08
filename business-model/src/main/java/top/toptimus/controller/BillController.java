package top.toptimus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.toptimus.common.enums.MetaTypeEnum;
import top.toptimus.common.result.Result;
import top.toptimus.service.BusinessUnitService;
import top.toptimus.service.domainService.PlaceService;
import top.toptimus.tokendata.TokenDataDto;

import java.util.List;

/**
 * 表单接口
 *
 * @author gaoyu
 */
@Api(value = "表单接口", tags = "表单管理")
@RestController
@RequestMapping(value = "/bill")
@Controller
public class BillController {
    @Autowired
    private PlaceService placeService;
    @Autowired
    private BusinessUnitService businessUnitService;

    /*
        表头URL处理接口
     */

    /**
     * 一览
     * 备查账查询数据(带有link信息)
     *
     * @param metaId     当前的基础资料备查账的metaId
     * @param selectType 当前选择的type页
     * @param pageNumber 当前页
     * @param pageSize   当前页大小
     * @return Result
     */
    @GetMapping(value = "/memorandvn/meta-id")
    public Result getMemorandvnData(
            @RequestParam String metaId
            , @RequestParam(required = false) String selectType
            , @RequestParam Integer pageNumber
            , @RequestParam Integer pageSize
    ) {
        return placeService.getMemorandvnData(metaId, selectType, pageNumber, pageSize);
    }

    /**
     * 创建表单
     *
     * @param businessUnitCode 业务单元Id
     * @param metaId           表头meta id
     * @return Result
     */
    @ApiOperation(value = "表单创建")
    @PutMapping(value = "/bill/business-unit-code/{businessUnitCode}/meta-id/{metaId}")
    public Result createBill(
            @PathVariable String businessUnitCode,
            @PathVariable String metaId
    ) {
        return placeService.createBill(businessUnitCode, metaId);
    }

    /**
     * 编辑的时候，存表头信息
     *
     * @param tokenDataDto 表头token
     * @param metaId       表头meta id
     * @return result
     */
    @ApiOperation(value = "表单详细:表头提交")
    @PostMapping(value = "/saveBill")
    public Result submitBillToken(
            @RequestParam String metaId
            , @RequestParam String schemaId
            , @RequestParam String id
            , @RequestBody TokenDataDto tokenDataDto
    ) {
        return placeService.saveBillToken(tokenDataDto, metaId, schemaId, id);
    }

    /**
     * 表单预览
     *
     * @param metaId  表头meta id
     * @param tokenId 表头tokenid
     * @return Result(toendataDTO)
     */
    @ApiOperation(value = "表单详细:表单预览")
    @GetMapping(value = "/getBillDetail")
    public Result getBillToken(
            @RequestParam String metaId
            , @RequestParam String tokenId

    ) {
        return placeService.getBillToken(metaId, tokenId);
    }

    /**
     * 删除单据
     *
     * @param id 表头tokenId
     * @return Result
     */
    @ApiOperation(value = "表单详细:删除单据")
    @DeleteMapping(value = "/deleteBill")
    public Result deleteBillToken(@RequestParam String id) {
        return placeService.deleteBillToken(id);
    }

//    /**
//     * 创建关联表单
//     *
//     * @param preMetaId  前置单据metaid
//     * @param preTokenId 前置单据表头tokenid
//     * @param metaId     需创建单据的metaid
//     * @return Result(TokenDataDTO)
//     */
//    @ApiOperation(value = "创建关联表单")
//    @PutMapping(value = "/bill/pre-meta-id/pre-token-id/meta-id")
//    public Result getRelBillToken(
//            @RequestParam String preMetaId
//            , @RequestParam String preTokenId
//            , @RequestParam String metaId
//    ) {
//        return placeService.getRelBillToken(preMetaId, preTokenId, metaId);
//    }


    /*
        表头URL处理接口   end
     */


    /*
        分录处理
     */

    /**
     * 删除分录  TODO
     *
     * @param entryMetaId  分录meta id
     * @param entryTokenId 分录token id
     * @param billTokenId  表头token id
     * @return result
     */
    @ApiOperation(value = "表单详细:删除分录")
    @DeleteMapping(value = "/deleteEntry")
    public Result deleteBillToken(
            @RequestParam String entryMetaId
            , @RequestParam String entryTokenId
            , @RequestParam String billTokenId
    ) {
        return placeService.deleteEntryToken(billTokenId, entryMetaId, entryTokenId);
    }

    /**
     * 新增分录
     *
     * @param entryMetaId 分录meta id
     * @param billTokenId 表头token
     * @return Result
     */
    @ApiOperation(value = "表单详细:新建分录")
    @PutMapping(value = "/entry/bill-token-id/entry-meta-id")
    public Result createEntryToken(
            @RequestParam String entryMetaId
            , @RequestParam String billTokenId
            , @RequestParam String billMetaId
    ) {
        return placeService.createEntryToken(billTokenId, billMetaId, entryMetaId);
    }

    /**
     * 存分录信息
     *
     * @param billTokenId  表头token id
     * @param tokenDataDto 分录token
     * @param entryMetaId  分录meta id
     * @return result
     */
    @ApiOperation(value = "表单详细:分录保存")
    @PostMapping(value = "/saveEntry")
    public Result submitEntryToken(
            @RequestParam String entryMetaId
            , @RequestParam String billTokenId
            , @RequestParam String billMetaId
            , @RequestParam MetaTypeEnum entryType
            , @RequestBody TokenDataDto tokenDataDto
    ) {
        return placeService.saveEntryToken(billTokenId, billMetaId, tokenDataDto, entryMetaId, entryType);
    }


    /**
     * 同步存分录信息
     *
     * @param entryMetaId  分录meta id
     * @param billTokenId  表头token id
     * @param billMetaId   表头meta id
     * @param tokenDataDto 数据
     * @return result 结果
     */
    @ApiOperation(value = "表单详细:分录保存")
    @PostMapping(value = "/entry/bill-token-id/entry-meta-id/sync")
    public Result submitEntryTokenSync(
            @RequestParam String entryMetaId
            , @RequestParam String billTokenId
            , @RequestParam String billMetaId
            , @RequestBody TokenDataDto tokenDataDto
    ) {
        return placeService.saveEntryTokenSync(billTokenId, billMetaId, tokenDataDto, entryMetaId);
    }


//    /**
//     * 批量新增分录
//     *
//     * @param entryMetaId 分录meta id
//     * @param billTokenId 表头token
//     * @param count       新增分录数量
//     * @return Result
//     */
//    @ApiOperation(value = "表单详细:批量新建分录")
//    @PutMapping(value = "/entries/bill-token-id/{billTokenId}/entry-meta-id/{entryMetaId}")
//    public Result createEntryToken(
//            @PathVariable String entryMetaId
//            , @PathVariable String billTokenId
//            , @RequestParam Integer count
//    ) {
//        return placeService.createEntryTokens(entryMetaId, billTokenId, count);
//    }

//    /**
//     * 批量存分录信息
//     *
//     * @param billTokenId   表头token id
//     * @param tokenDataDtos 分录tokens
//     * @param entryMetaId   分录meta id
//     * @return result
//     */
//    @ApiOperation(value = "表单详细:批量分录保存")
//    @PostMapping(value = "/entries/bill-token-id/{billTokenId}/entry-meta-id/{entryMetaId}")
//    public Result submitEntryToken(
//            @PathVariable String entryMetaId
//            , @PathVariable String billTokenId
//            , @RequestBody List<TokenDataDto> tokenDataDtos
//    ) {
//        return placeService.saveEntryTokens(billTokenId, tokenDataDtos, entryMetaId);
//    }
    /*
        分录URL处理接口   end
     */

    /*
        单据引用备查账URL处理接口
     */

    /**
     * 查询单据引用的备查账数据
     *
     * @param memorandvnMetaId 主数据备查帐的metaId
     * @param billTokenId      表头tokenId
     * @param billMetaId       主数据表头metaId
     * @return Result
     */
    @ApiOperation(value = "表单详细:查询单据引用的备查账数据")
    @GetMapping(value = "/entry/bill-meta-id/bill-token-id/memorandvn-meta-id")
    public Result getMemorandvnTokenDatas(
            @RequestParam String memorandvnMetaId
            , @RequestParam String billTokenId
            , @RequestParam String billMetaId
    ) {
        return placeService.getMemorandvnTokenDatas(billTokenId, billMetaId, memorandvnMetaId);
    }

    /**
     * 单据引用备查账
     *
     * @param billMetaId       单据主数据meta
     * @param billTokenId      单据表头tokenid
     * @param memorandvnMetaId 引用备查账的metaid
     * @param tokenIds         引用备查账的tokenids
     * @return Result
     */
    @ApiOperation(value = "表单详细:单据引用备查账")
    @PostMapping(value = "/entry/bill-meta-id/bill-token-id/memorandvn-meta-id")
    public Result createBillQuoteMemorandvn(
            @RequestParam String billMetaId
            , @RequestParam String billTokenId
            , @RequestParam String memorandvnMetaId
            , @RequestBody List<String> tokenIds
    ) {
        return placeService.createBillQuoteMemorandvn(billMetaId, billTokenId, memorandvnMetaId, tokenIds);
    }

    /**
     * 删除单据对备查账的引用
     *
     * @param billMetaId        单据主数据meta
     * @param billTokenId       单据表头tokenid
     * @param memorandvnMetaId  引用备查账的metaid
     * @param memorandvntokenId 引用备查账的tokenid
     * @return Result
     */
//    @ApiOperation(value = "表单详细:删除单据对备查账的引用")
//    @GetMapping(value = "/entry/bill-meta-id/bill-token-id/memorandvn-meta-id")
//    public Result deleteBillQuoteMemorandvn(
//            @RequestParam String billMetaId
//            , @RequestParam String billTokenId
//            , @RequestParam String memorandvnMetaId
//            , @RequestParam String memorandvntokenId
//    ) {
//        return placeService.deleteBillQuoteMemorandvn(billMetaId, billTokenId, memorandvnMetaId, memorandvntokenId);
//    }
    /*
        单据引用备查账URL处理接口  end
     */


    /**
     * 获取业务单元可新增/下推meta
     *
     * @param businessUnitCode 业务单元Id
     * @param metaId           metaId
     * @param tokenId          tokenId
     * @return Result
     */
    @ApiOperation(value = "获取业务单元可新增/下推meta")
    @GetMapping(value = "/getPushDownMeta")
    public Result getPushDownMeta(@RequestParam String businessUnitCode
            , @RequestParam String metaId
            , @RequestParam String tokenId) {
        return placeService.getPushDownMeta(businessUnitCode, metaId, tokenId);
    }

    /**
     * 获取业务节点的关联meta
     *
     * @param businessUnitCode 业务单元Id
     * @param metaId
     * @return Result
     */
    @ApiOperation(value = "获取业务节点的关联meta")
    @GetMapping(value = "/getRelMeta")
    public Result getRelMeta(@RequestParam String businessUnitCode, @RequestParam(required = false) String metaId) {
        return placeService.getRelMeta(businessUnitCode, metaId);
    }

    /**
     * 获取业务节点的关联meta下的数据
     *
     * @param businessUnitCode 业务单元Id
     * @param preMetaId        前置metaId
     * @param preTokenId       前置tokenId
     * @param metaId           当前metaId
     * @return Result
     */
    @ApiOperation(value = "获取业务节点的关联meta下的数据")
    @GetMapping(value = "/getRelData")
    public Result getRelData(@RequestParam String businessUnitCode
            , @RequestParam(required = false) String preMetaId
            , @RequestParam(required = false) String preTokenId
            , @RequestParam String metaId) {
        return placeService.getRelData(businessUnitCode, preMetaId, preTokenId, metaId);
    }

    /**
     * 手动下推
     *
     * @param businessUnitCode 业务单元Id
     * @param preMetaId        前置metaId
     * @param preTokenId       前置tokenId
     * @param metaId           下推到的metaId
     * @return Result
     */
    @ApiOperation(value = "获取业务节点的关联meta下的数据")
    @GetMapping(value = "/pushDown")
    public Result pushDown(@RequestParam String businessUnitCode
            , @RequestParam String preMetaId
            , @RequestParam String preTokenId
            , @RequestParam String metaId) {
        return placeService.pushDown(businessUnitCode, preMetaId, preTokenId, metaId);
    }

    /**
     * 获取表单分录meta
     *
     * @param billMetaId 单据主数据meta
     * @return Result
     */
    @ApiOperation(value = "获取表单分录meta")
    @GetMapping(value = "/getEntryMeta")
    public Result getEntryMeta(@RequestParam String billMetaId) {
        return placeService.getEntryMetas(billMetaId);
    }

    /**
     * 查询分录数据
     *
     * @param entryMetaId 分录metaid
     * @param billMetaId  表头meta
     * @param billTokenId 表头tokenid
     * @return Result
     */
    @ApiOperation(value = "表单详细:查询分录数据")
    @GetMapping(value = "/entry/bill-meta-id/bill-token-id/entry-meta-id")
    public Result getEntryToken(
            @RequestParam String entryMetaId
            , @RequestParam String billMetaId
            , @RequestParam String billTokenId
    ) {
        return placeService.getEntryToken(billMetaId, billTokenId, entryMetaId);
    }

    /**
     * 提交表单
     *
     * @param tokenDataDto 表头token
     * @param metaId       表头meta id
     * @return result
     */
    @ApiOperation(value = "表单详细:表头保存")
    @PostMapping(value = "/submit/meta-id")
    public Result submit(
            @RequestParam String businessUnitCode
            , @RequestParam String metaId
            , @RequestBody TokenDataDto tokenDataDto
    ) {
        return placeService.submit(businessUnitCode, tokenDataDto, metaId);
    }

    /**
     * 调存储过程
     *
     * @param metaId 表头meta id
     * @return result
     */
    @ApiOperation(value = "提交后调用存储过程")
    @GetMapping(value = "/submit/storeProcedure")
    public Result submitStoreProcedure(@RequestParam String metaId, @RequestParam String tokenId
    ) {
        return placeService.submitStoreProcedure(metaId, tokenId);
    }

    /**
     * 单据预览schema查询接口
     *
     * @param id
     * @return result
     */
    @ApiOperation(value = "单据预览schema查询接口")
    @GetMapping(value = "/preview")
    public Result getPreview(@RequestParam String id) {
        return placeService.getPreview(id);
    }
}
