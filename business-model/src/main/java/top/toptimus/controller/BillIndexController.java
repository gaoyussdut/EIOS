package top.toptimus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.toptimus.common.enums.BillOperationEnum;
import top.toptimus.common.enums.process.MetaRelEnum;
import top.toptimus.common.result.ResultContext;
import top.toptimus.entity.Memorandvn.query.MemorandvnQueryFacadeEntity;
import top.toptimus.entity.meta.query.ConfigQueryEntity;
import top.toptimus.entity.meta.query.MetaQueryFacadeEntity;
import top.toptimus.meta.MetaRelation.MetaAuthRelationDTO;
import top.toptimus.meta.MetaRelation.MetaRelationDTO;
import top.toptimus.meta.MetaRelation.MetaViewInfoDTO;
import top.toptimus.model.memorandumModel.*;
import top.toptimus.service.domainService.MetaService;

import java.util.List;


/**
 * Created by JiangHao on 2019/1/28.
 */
@Api(value = "表单索引接口", tags = "表单索引管理")
@RestController
@RequestMapping(value = "/bill")
@Controller
public class BillIndexController {

    @Autowired
    private MetaService metaService;
    @Autowired
    private ConfigQueryEntity configQueryEntity;
    @Autowired
    private MemorandvnQueryFacadeEntity memorandvnQueryFacadeEntity;
    @Autowired
    private MetaQueryFacadeEntity metaQueryFacadeEntity;

    /**
     * 一览的索引link信息
     *
     * @param metaId 备查账metaId
     * @return ResultContext
     */
    @ApiOperation(value = "一览 索引信息")
    @GetMapping(value = "/outlineIndex/meta-id/{metaId}")
    public ResultContext memorandumOutlineIndexLink(@PathVariable String metaId) {
        return new MemorandumOutlineIndexLinkModel(
                memorandvnQueryFacadeEntity.getMemorandvnByMetaId(metaId)
        ).getResultContext();
    }

    /**
     * 获取备查账一览的URL详细信息（预览备查账索引、获取一览数据、meta、tabs、新建）
     *
     * @param metaId metaId
     * @return ResultContext
     */
    @ApiOperation(value = "一览 详细的URL信息")
    @GetMapping(value = "/outlineDetail/meta-id/{metaId}")
    public ResultContext memorandumOutlineDetailLink(@PathVariable String metaId) {
        // 根据备查账metaId查找 对应的表单metaId
        List<MetaRelationDTO> metaRelationDTOS = metaQueryFacadeEntity.findByMasterMemorandvnMetaId(metaId);
        if (metaRelationDTOS.isEmpty() || metaRelationDTOS.size() > 1) {
            throw new RuntimeException("备查账查找单据配置 异常");
        } else {
            return new MemorandumOutlineDetailLinkModel(
                    metaId
                    , metaRelationDTOS.get(0)
                    , configQueryEntity.findMetaSearchConfigByMetaId(metaId)
                    , metaId
                   // , metaService.getSelfDefiningMeta(metaId).getData().toString() TODO 用户自定义，通过主数据meta和用户ID查找视图meta
            ).getResultContext();
        }
    }

    /**
     * 预览单据的URL索引信息 (预览单据的详细信息的URL、userControl类型、title)
     *
     * @param billMetaId  表头metaId
     * @param billTokenId 表头tokenId
     * @return ResultContext
     */
    @ApiOperation(value = "预览 索引信息")
    @GetMapping(value = "/previewIndex/bill-meta-id/{billMetaId}/bill-token-id/{billTokenId}")
    public ResultContext memorandumPreviewIndexLink(@PathVariable String billMetaId
            , @PathVariable String billTokenId) {
        return new MemorandumPreviewIndexLinkModel(
                billTokenId
                , billMetaId
                , metaService.getMetaAuthRelation(billMetaId)
        ).getResultContext();
    }

    /**
     * 预览单据的URL详细信息 (增删改查、meta信息等等)
     *
     * @param billMetaId  表头metaId
     * @param billTokenId 表头tokenId
     * @param metaId      要查询的URL详细信息的metaId
     * @return ResultContext
     */
    @ApiOperation(value = "预览 详细URL信息")
    @GetMapping(value = "/previewDetail/bill-meta-id/{billMetaId}/bill-token-id" +
            "/{billTokenId}/meta-id/{metaId}")
    public ResultContext memorandumPreviewDetailLink(@PathVariable String billMetaId
            , @PathVariable String billTokenId
            , @PathVariable String metaId) {
        return new MemorandumPreviewLinkModel(
                billTokenId
                , metaId
                , billMetaId
                , metaService.getMetaAuthRelation(billMetaId)
        ).getResultContext();
    }

    /**
     * 创建或编辑单据 URL的索引信息
     *
     * @param billMetaId  表头metaId
     * @param billTokenId 表头tokenId （不传为新建、传为编辑）
     * @return ResultContext
     */
    @ApiOperation(value = "创建或编辑单据 索引信息")
    @GetMapping(value = "/createOrEditIndex/bill-meta-id/{billMetaId}/bill-operation/{billOperation}")
    public ResultContext createOrEditMemorandumIndexLink(@PathVariable String billMetaId
            , @PathVariable BillOperationEnum billOperation
            , @RequestParam(required = false) String billTokenId) {
        return new CreateOrEditMemorandumIndexLinkModel(
                billMetaId
                ,billTokenId
                , billOperation
                , metaService.getMetaAuthRelation(billMetaId)
        ).getResultContext();
    }

    /**
     * 创建或编辑单据 URL详细信息 (查看、提交、获取新建单据等等)
     *
     * @param billMetaId    表头metaId
     * @param metaId        要查询的URL详细信息的metaId
     * @param billOperation 单据操作类型
     * @param billTokenId   表头tokenId （不传为新建、传为编辑）
     * @return ResultContext
     */
    @ApiOperation(value = "创建或编辑单据 URL详细信息")
    @GetMapping(value = "/createOrEditDetail/bill-meta-id/{billMetaId}/meta-id/{metaId}/bill-operation/{billOperation}")
    public ResultContext createOrEditMemorandumDetailLink(@PathVariable String billMetaId
            , @PathVariable String metaId
            , @PathVariable BillOperationEnum billOperation
            , @RequestParam(required = false) String billTokenId) {
        return new CreateOrEditMemorandumLinkModel(
                billMetaId
                , billTokenId
                , metaId
                , metaService.getMetaAuthRelation(billMetaId)
                , billOperation
        ).getResultContext();
    }

    /**
     * 预览单据中 创建关联单据索引信息
     *
     * @param billMetaId  表头metaId
     * @param billTokenId 表头token
     * @param metaId      关联单据的备查账metaId
     * @return ResultContext
     */
    @ApiOperation(value = "预览单据中 创建关联单据")
    @GetMapping(value = "/createEntryIndex/bill-meta-id/{billMetaId}/bill-token-id/{billTokenId}/meta-id/{metaId}")
    public ResultContext memorandumPreviewEntryHandlerIndexLink(@PathVariable String billMetaId
            , @PathVariable String billTokenId
            , @PathVariable String metaId
    ) {
        // 前置单据的单据信息
        MetaAuthRelationDTO metaAuthRelationDTO = metaService.getMetaAuthRelation(billMetaId);
        // 关联备查账的单据的信息
        MetaAuthRelationDTO associatMetaAuthRelationDTO = null;
        for (MetaViewInfoDTO metaViewInfoDTO : metaAuthRelationDTO.getRelMetaId()) {
            // 如果是备查账新建单据  找到备查账的具体单据信息
            if (metaId.equals(metaViewInfoDTO.getMasterDataMetaId())
                    && MetaRelEnum.CERTIFICATE.equals(metaViewInfoDTO.getMetaRelEnum())) {
                associatMetaAuthRelationDTO = metaService.getMetaAuthRelation(metaViewInfoDTO.getRelMetaId());
                break;
            }
        }
        return new MemorandumPreviewEntryHandlerIndexLinkModel(
                billMetaId
                , billTokenId
                , metaId
                , metaAuthRelationDTO // 前置单据的单据信息
                , associatMetaAuthRelationDTO // 关联备查账的单据的信息
        ).getResultContext();
    }


}
