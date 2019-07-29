package top.toptimus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.toptimus.common.result.Result;
import top.toptimus.meta.FKeyCaptionDto;
import top.toptimus.meta.MetaRelation.MasterBillMetaRelationDTO;
import top.toptimus.meta.MetaRelation.MetaAuthRelationDTO;
import top.toptimus.meta.MetaRelation.MetaRelationDTO;
import top.toptimus.meta.TalendModelMetaDto;
import top.toptimus.meta.TokenMetaInfoDTO;
import top.toptimus.meta.TokenMetaInformationDto;
import top.toptimus.meta.metaview.MetaInfoDTO;
import top.toptimus.service.domainService.MetaService;

import java.util.List;
import java.util.Map;

@Api(tags = "meta管理")
@RestController
@RequestMapping(value = "/meta")
@Controller
public class MetaController {

    @Autowired
    private MetaService metaService;

    /**
     * 根据metaID获取表头MetaFKeyListDTO
     *
     * @param metaId meta id
     * @return MetaFKeyListDTO
     */
    @ApiOperation(value = "获取metaFkey")
    @GetMapping(value = "/getMetaFkey")
    public Result getTokenMetaDto(@RequestParam String metaId
            , @RequestParam(defaultValue = "true", required = false) boolean isReadonly) {
        return metaService.getCaptionAndKeyByMetaId(metaId, isReadonly);
    }

    /**
     * 取得对应meta的信息一览
     *
     * @param metaId meta id
     * @return meta info
     */
    @ApiOperation(value = "取meta编辑画面")
    @RequestMapping(value = {"/findByMetaId"}, method = RequestMethod.GET)
    public Result findByMetaId(@RequestParam String metaId) {
        return metaService.getMetaInfoByMetaId(metaId);
    }

    //------------------------meta 维护配置接口--start-----------------------

    /**
     * 查询所有Fkeys
     *
     * @return meta字段
     */
    @ApiOperation(value = "查询所有Fkeys")
    @RequestMapping(value = {"/findAllFkeys"}, method = RequestMethod.GET)
    public Result findAllFkeys(@RequestParam(required = false) String type, @RequestParam Integer pageNo, @RequestParam Integer pageSize) {
        return metaService.findAllFkeys(type, pageNo, pageSize);
    }

    /**
     * 查询所有meta信息
     *
     * @param pageNo   页号
     * @param pageSize 页宽
     * @return meta信息定义，token名和类型
     */
    @ApiOperation(value = "查询所有meta")
    @RequestMapping(value = {"/findAllTokenMetaInformation"}, method = RequestMethod.GET)
    public Result findAllTokenMetaInformation(@RequestParam Integer pageNo, @RequestParam Integer pageSize) {
        return metaService.findAllTokenMetaInformation(pageNo, pageSize);
    }

    /**
     * 追加Meta
     *
     * @param tokenMetaInforMationDtos meta信息定义，token名和类型
     * @return Result
     */
    @ApiOperation(value = "追加Meta")
    @PostMapping(value = "/saveTokenMetaInformationDao")
    public Result saveAllTokenMetaInformationDao(@RequestBody List<TokenMetaInformationDto> tokenMetaInforMationDtos) {
        return metaService.saveAllTokenMetaInformationDao(tokenMetaInforMationDtos);
    }

    /**
     * 删除Meta
     *
     * @param tokenMetaInforMationDtos meta信息定义，token名和类型
     * @return Result
     */
    @ApiOperation(value = "删除Meta")
    @PostMapping(value = "/deleteTokenMetaInformationDao")
    public Result deleteTokenMetaInformationDao(@RequestBody List<TokenMetaInformationDto> tokenMetaInforMationDtos) {
        return metaService.deleteTokenMetaInformationDao(tokenMetaInforMationDtos);
    }

    /**
     * 根据meta取得Fkey
     *
     * @param metaId meta id
     * @return 字段
     */
    @ApiOperation(value = "根据metaid取得Fkeys")
    @RequestMapping(value = {"/findFkeyByMetaId"}, method = RequestMethod.GET)
    public Result findFkeyByMetaId(@RequestParam String metaId) {
        return metaService.findFkeyByMetaId(metaId);
    }

    /**
     * 追加多条MetaInfos
     *
     * @param metaInfoDTOS meta列表
     * @return Result
     */
    @ApiOperation(value = "追加多条MetaInfos")
    @PostMapping(value = "/saveMetaInfoDTOs")
    public Result save(@RequestBody List<MetaInfoDTO> metaInfoDTOS) {
        return metaService.saveMetaInfoDTO(metaInfoDTOS);
    }

    /**
     * 创建表
     *
     * @param tableName 表名
     * @param metaIds   meta id列表
     * @return Result
     */
    @ApiOperation(value = "创建表")
    @PostMapping(value = "/createTable")
    public Result createTable(@RequestParam String tableName, @RequestBody List<String> metaIds) {
        return metaService.createTable(tableName, metaIds);
    }

    /**
     * 查找全部的key的默认caption
     *
     * @return List<FKeyCaptionDao>
     */
    @ApiOperation(value = "查找全部的key的默认caption")
    @GetMapping(value = "/getAllKeyCaption")
    public Result getAllKeyCaption() {
        return metaService.getAllKeyCaption();
    }

    /**
     * 根据key查找对应的默认caption
     *
     * @return List<FKeyCaptionDao>
     */
    @ApiOperation(value = "查找全部的key的默认caption")
    @PostMapping(value = "/getAllKeyCaptionByKeys")
    public Result getAllKeyCaptionByKeys(@RequestBody List<String> keys) {
        return metaService.getAllKeyCaptionByKeys(keys);
    }

    /**
     * 根据x_pk_x_talend_id 查询所有的metaJson
     *
     * @param x_pk_x_talend_id x_pk_x_talend_id
     * @return List<TalendModelMeta>
     */
    @ApiOperation(value = "根据x_pk_x_talend_id查找所有的metaJson")
    @GetMapping(value = "/getAllByXPkXTalendId")
    public Result getAllByXPkXTalendId(@RequestParam String x_pk_x_talend_id) {
        return metaService.getAllByXPkXTalendId(x_pk_x_talend_id);
    }

    /**
     * 保存全部TalendModelMeta
     *
     * @param talendModelMetas talendModelMetas
     */
    @ApiOperation(value = "保存TalendModel下的meta")
    @PostMapping(value = "/saveAllTalendModelMeta")
    public Result saveAllTalendModelMeta(@RequestBody Map<String, List<TalendModelMetaDto>> talendModelMetas) {
        return metaService.saveAllTalendModelMeta(talendModelMetas);
    }

    /**
     * 比较meta是否和数据库中的meta一致
     *
     * @param talendModelMetas talendModelMetas
     */
    @ApiOperation(value = "比较meta是否和数据库中的meta一致")
    @PostMapping(value = "/equalsTalendModelMeta")
    public Result equalsTalendModelMeta(
            @RequestBody Map<String, List<TalendModelMetaDto>> talendModelMetas) {
        return metaService.equalsTalendModelMeta(talendModelMetas);
    }

    /**
     * 获取用户自定义MetaId
     *
     * @param userId           用户Id
     * @param memorandvnMetaId 备查帐ID
     */
    @ApiOperation(value = "获取用户自定义MetaId")
    @GetMapping(value = "/getSelfDefiningMeta")
    public Result getSelfDefiningMeta(@RequestParam String userId, @RequestParam String memorandvnMetaId) {
        return metaService.getSelfDefiningMeta(userId, memorandvnMetaId);
    }

    /**
     * 保存用户自定义MetaId
     *
     * @param userId           用户Id
     * @param memorandvnMetaId 备查帐ID
     * @param tokenMetaInfoDTO meta字段信息
     */
    @ApiOperation(value = "获取用户自定义MetaId")
    @PostMapping(value = "/saveSelfDefiningMeta")
    public Result getSelfDefiningMeta(@RequestParam String userId, @RequestParam String memorandvnMetaId, @RequestBody TokenMetaInfoDTO tokenMetaInfoDTO) {
        return metaService.saveSelfDefiningMeta(userId, memorandvnMetaId, tokenMetaInfoDTO);
    }

    /**
     * 主数据meta获取单据内meta关系(查詢)
     *
     * @param metaId 主数据Meta
     */
    @ApiOperation(value = "主数据meta获取单据内meta关系")
    @GetMapping(value = "/getMasterDataMetaRelation")
    public Result getMasterDataMetaRelation(@RequestParam String metaId) {
        return metaService.getMasterDataMetaRelation(metaId);
    }

    /**
     * 根据表头主数据billMetaIdList查该表单关联的主数据meta
     *
     * @param billMetaIdList billMetaIdList meta id列表
     */
    @ApiOperation(value = "根据表头主数据metaid查该表单关联的主数据meta")
    @GetMapping(value = "/getAllMasterDataMetaRelation")
    public Result getAllMasterDataMetaRelation(@RequestParam List<String> billMetaIdList) {
        return metaService.getAllMasterDataMetaRelation(billMetaIdList);
    }

    /**
     * 保存主数据meta单据内meta关系(保存)
     *
     * @param metaRelationDTO meta关系dto
     */
    @ApiOperation(value = "主数据meta获取单据内meta关系")
    @PostMapping(value = "/saveMasterDataMetaRelation")
    public Result saveMasterDataMetaRelation(@RequestBody MetaRelationDTO metaRelationDTO) {
        return metaService.saveMasterDataMetaRelation(metaRelationDTO);
    }


    /**
     * 根据表头主数据metaid查该表单关联的主数据meta
     *
     * @param billMetaId 表头meta id
     */
    @ApiOperation(value = "根据表头主数据metaid查该表单关联的主数据meta")
    @GetMapping(value = "/getMasterBillMetaRelation")
    public Result getMasterBillMetaRelation(@RequestParam String billMetaId) {
        return metaService.getMasterBillMetaRelation(billMetaId);
    }

    /**
     * 保存主数据meta的关联关系
     *
     * @param masterBillMetaRelationDTO 单据表单主数据与关联表单的的关系
     */
    @ApiOperation(value = "保存主数据meta的关联关系")
    @PostMapping(value = "/saveMasterBillMetaRelation")
    public Result saveMasterBillMetaRelation(@RequestBody MasterBillMetaRelationDTO masterBillMetaRelationDTO) {
        return metaService.saveMasterBillMetaRelation(masterBillMetaRelationDTO);
    }

    //------------------------------------------单据meta关系配置画面接口--------------------------------------------------

    /**
     * 根据meta数据类型获取meta一览
     *
     * @param metaDataType meta数据类型
     * @param pageNo       页码
     * @param pageSize     页宽
     */
    @ApiOperation(value = "获取meta一览")
    @GetMapping(value = "/getAllMetaByMetaDataType")
    public Result getAllMeta(@RequestParam String metaDataType, @RequestParam Integer pageNo, @RequestParam Integer pageSize) {
        return metaService.getAllMetaByMetaDataType(metaDataType, pageNo, pageSize);
    }

    /**
     * 根据meta类型获取meta一览
     *
     * @param metaType     meta类型
     * @param metaDataType meta数据类型
     * @param pageNo       页码
     * @param pageSize     页宽
     */
    @ApiOperation(value = "获取meta一览")
    @GetMapping(value = "/getAllMetaByMetaType")
    public Result getAllMetaByMetaType(@RequestParam String metaType, @RequestParam String metaDataType, @RequestParam Integer pageNo, @RequestParam Integer pageSize) {
        return metaService.getAllMetaByMetaType(metaType, metaDataType, pageNo, pageSize);
    }

    /**
     * 获取主数据meta视图meta
     */
    @ApiOperation(value = "获取主数据meta视图meta")
    @GetMapping(value = "/getViewMetaByMasterDataMeta")
    public Result getViewMetaByMasterDataMeta(@RequestParam String metaId) {
        return metaService.getViewMetaByMasterDataMeta(metaId);
    }

    /**
     * 根据指定的roleid获取主数据meta视图meta
     */
    @ApiOperation(value = "根据指定的roleid获取主数据meta视图meta")
    @GetMapping(value = "/getMetaAuthRelation")
    public Result getMetaAuthRelation(@RequestParam String billMetaId, String roleId) {
        return metaService.getMetaAuthRelation(billMetaId, roleId);
    }

    /**
     * 根据metaIds获取metaId的定义信息
     *
     * @param metaIds metaIds
     */
    @ApiOperation(value = "根据metaIds获取metaId的定义信息")
    @PostMapping(value = "/getMetaFormationsByMetaIds")
    public Result getMetaFormationsByMetaIds(@RequestBody List<String> metaIds) {
        return metaService.getMetaFormationsByMetaIds(metaIds);
    }

    /**
     * 保存role和单据视图meta的关系
     *
     * @param metaAuthRelationDTO role和单据视图meta的关系
     */
    @ApiOperation(value = "保存role和单据视图meta的关系")
    @PostMapping(value = "/saveMetaAuthRelation")
    public Result saveMetaAuthRelation(@RequestBody MetaAuthRelationDTO metaAuthRelationDTO) {
        return metaService.saveMetaAuthRelation(metaAuthRelationDTO);
    }

    /**
     * 根据主数据meta查询已配置的单据的权限
     *
     * @param metaId 单据数据metaid
     * @return Result
     */
    @ApiOperation(value = "根据主数据meta查询已配置的单据的权限")
    @GetMapping(value = "/getMetaAuthRelationDaosByMetaId")
    public Result getMetaAuthRelationDaosByMetaId(@RequestParam String metaId) {
        return metaService.getMetaAuthRelationDaosByMetaId(metaId);
    }


    /**
     * 根据主数据metaID查找旗下的视图metaId并修改旗下的Caption
     *
     * @param fKeyCaptionDtos 字段Fkey和描述caption
     * @param masterMetaId    主数据metaID
     * @return Result
     */
    @ApiOperation(value = "根据主数据metaID查找旗下的视图metaId并修改旗下的Caption")
    @PostMapping(value = "/updateMetaCaption")
    public Result updateMetaCaption(@RequestBody List<FKeyCaptionDto> fKeyCaptionDtos, @RequestParam String masterMetaId) {
        return metaService.updateMetaCaption(fKeyCaptionDtos, masterMetaId);
    }

    /**
     * 获取所有表单的配置关联信息(表单的表头、分录、关联的备查账的metaId和关联类型)
     *
     * @return Result
     */
    @ApiOperation(value = "获取所有表单的配置关联信息")
    @GetMapping(value = "/getBillRelations")
    public Result getAllBillRelationInfo() {
        return metaService.getAllBillRelationInfo();
    }

    /**
     * 根据metaId查询SignGroupDTO信息
     *
     * @param metaId 单据metaId
     * @return 汇签组信息
     */
    @ApiOperation(value = "根据单据metaId获取汇签组信息")
    @GetMapping(value = "/getSignGroupDTOBySignMetaId")
    public Result findSignGroupDTOBySignMetaId(@RequestParam String metaId) {
        return metaService.findBySignMetaId(metaId);
    }

    /**
     * 根据主数据的表头metaId获取单据的关联信息
     *
     * @param billMasterMetaId 表头的主数据metaId
     * @return Result
     */
    @ApiOperation(value = "根据主数据的表头metaId获取单据的关联信息")
    @GetMapping(value = "/getBillRelationInfoByBillMasterMetaId")
    public Result getBillRelationInfoByBillMasterMetaId(@RequestParam String billMasterMetaId) {
        return metaService.getBillRelationInfoByBillMasterMetaId(billMasterMetaId);
    }

    /**
     * 根据备查帐metaId查询单据
     *
     * @param memorandvnMetaId 备查账metaId
     * @return Result(List<MetaRelationDTO>)
     */
    @ApiOperation(value = "根据备查帐metaId查询单据")
    @GetMapping(value = "/findByMasterMemorandvnMetaId")
    public Result findByMasterMemorandvnMetaId(@RequestParam String memorandvnMetaId) {
        return metaService.findByMasterMemorandvnMetaId(memorandvnMetaId);
    }

}
