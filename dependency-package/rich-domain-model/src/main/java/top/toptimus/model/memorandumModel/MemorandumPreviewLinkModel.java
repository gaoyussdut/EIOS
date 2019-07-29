package top.toptimus.model.memorandumModel;

import com.google.common.collect.Lists;
import top.toptimus.common.enums.BillOperationEnum;
import top.toptimus.common.enums.RequestMethodEnum;
import top.toptimus.common.enums.process.MetaRelEnum;
import top.toptimus.common.result.ResultContext;
import top.toptimus.common.result.ResultLink;
import top.toptimus.common.result.ResultUserControl;
import top.toptimus.constantConfig.MemorandumConstants;
import top.toptimus.constantConfig.ServiceURLConstants;
import top.toptimus.meta.MetaRelation.MetaAuthRelationDTO;
import top.toptimus.meta.MetaRelation.MetaViewInfoDTO;
import top.toptimus.resultModel.BaseResultLinkInfoModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 构建预览单据内的具体的URL信息
 * Created by JiangHao on 2019/1/22.
 */
public class MemorandumPreviewLinkModel extends BaseResultLinkInfoModel {

    // 获取分录一览的title
    private static final String GET_ENTRY_OUTLINE_VIEW_TITLE = "获取分录一览";
    // 获取表头数据title
    private static final String GET_BILL_HEADER_DATA_TITLE = "获取表头的数据";
    // 编辑按钮
    private static final String EDIT_BUTTON = "editButton";
    // 编辑按钮title
    private static final String EDIT_BUTTON_TITLE = "编辑";
    // 预览备查账
    private static final String MEMORANDUM_PREVIEW = "预览备查账";
    // 预览
    private static final String PREVIEW = "preview";
    // 一览
    private static final String OUTLINE = "outline";
    // 一览title
    private static final String OUTLINE_TITLE = "备查账一览";
    // 单据引用备查账
    private static final String BILL_REFERENCE_MEMORANDUM = "billReferenceMemorandum";
    // 单据引用备查账title
    private static final String BILL_REFERENCE_MEMORANDUM_TITLE = "单据引用备查账";
    // 解除单据对备查账的引用关系
    private static final String RELIEVE_BILL_REFERENCE_MEMORANDUM = "relieveBillReferenceMemorandum";
    // 解除单据对备查账的引用关系title
    private static final String RELIEVE_BILL_REFERENCE_MEMORANDUM_TITLE = "解除单据对备查账的引用关系";
    // 创建关联单据索引
    private static final String CREATE_RELEVANCE_BILL_INDEX = "createRelevanceBillIndex";
    // 创建关联单据索引
    private static final String CREATE_RELEVANCE_BILL_INDEX_TITLE = "创建关联单据索引";
    // 获取单条分录
    private static final String GET_SINGLE_ENTRY = "getSingleEntry";
    // 获取单条分录title
    private static final String GET_SINGLE_ENTRY_TITLE = "获取单条分录数据";

    private String billMetaId; // 表头的metaId
    private String billTokenId; // 表头tokenId
    private MetaAuthRelationDTO metaAuthRelationDTO; // 根据权限查询表头meta所能查看的分录、备查账meta

    public MemorandumPreviewLinkModel(
            String billTokenId
            , String metaId
            , String billMetaId
            , MetaAuthRelationDTO metaAuthRelationDTO) {
        super.metaId = metaId;
        this.billMetaId = billMetaId;
        this.billTokenId = billTokenId;
        this.metaAuthRelationDTO = metaAuthRelationDTO;
        this.buildAllResultLinks();
        super.resultContext = new ResultContext(
                this.resultLinks
        );

    }

    /**
     * 构建全部link的信息
     */
    private void buildAllResultLinks() {
        this.resultLinks = new ArrayList<>();
        for (MetaViewInfoDTO metaViewInfoDTO : this.metaAuthRelationDTO.getRelMetaId()) {
            if (this.metaId.equals(metaViewInfoDTO.getMasterDataMetaId())) {
                Map<String, List<ResultLink>> resultLinkMap = new HashMap<>();
                // 根据关联的单据类型 拼接URL信息
                switch (metaViewInfoDTO.getMetaRelEnum()) {
                    case BILL_HEADER:
                        buildBillHeaderURLInfo(metaViewInfoDTO, resultLinkMap, this.billTokenId);
                        break;
                    case CERTIFICATE:
                    case MEMORANDVN:
                        buildReferenceMemorandvn(metaViewInfoDTO, resultLinkMap, this.billTokenId);
                        break;
                    case ENTRY:
                        buildEntryURLInfo(metaViewInfoDTO, resultLinkMap, this.billTokenId, this.billMetaId);
                        break;
                    default:
                        break;
                }
                this.resultLinks.add(
                        new ResultUserControl(
                                metaViewInfoDTO.getMetaName()
                                , PREVIEW_USER_CONTROL + metaViewInfoDTO.getUsercontrol()
                                , metaViewInfoDTO.getMasterDataMetaId()
                                , resultLinkMap
                        )
                );
            }
        }
    }

    /**
     * 构建分录的url信息
     */
    private void buildEntryURLInfo(MetaViewInfoDTO metaViewInfoDTO
            , Map<String, List<ResultLink>> resultLinks
            , String billTokenId, String billMetaId) {
        resultLinks.put(DATA, Lists.newArrayList(
                new ResultLink(
                        GET_ENTRY_OUTLINE_VIEW_TITLE
                        , ServiceURLConstants.BILL_ENTRY_OPERATION_URL
                        + "/" + URL_ARGS_BILL_HEADER_TOKEN_ID + "/" + billTokenId
                        + "/" + URL_ARGS_BILL_ENTRY_META_ID + "/" + metaViewInfoDTO.getMasterDataMetaId()
                        + "?" + URL_ARGS_HUMP_BILL_META_ID + "=" + billMetaId
                        , Lists.newArrayList(RequestMethodEnum.GET)
                ))
        );
        resultLinks.put(GET_SINGLE_ENTRY, Lists.newArrayList(
                new ResultLink(
                        GET_SINGLE_ENTRY_TITLE
                        , ServiceURLConstants.CREATE_AND_READ_BILL_URL
                        + "/" + metaViewInfoDTO.getMasterDataMetaId()
                        + "/" + URL_ARGS_TOKEN_ID
                        + "/" + URL_NEED_INJECT_ARGS_TOKEN_ID
                        , Lists.newArrayList(RequestMethodEnum.GET)
                )
        ));
        resultLinks.put(META, Lists.newArrayList(
                new ResultLink(
                        metaViewInfoDTO.getMetaName()
                        , ServiceURLConstants.GET_META_URL + "?"
                        + MemorandumConstants.META_ID + "=" + metaViewInfoDTO.getViewMetaId()
                        , Lists.newArrayList(RequestMethodEnum.GET)
                ))
        );
    }

    /**
     * 构建表头的url信息
     */
    private void buildBillHeaderURLInfo(MetaViewInfoDTO metaViewInfoDTO
            , Map<String, List<ResultLink>> resultLinks, String billTokenId) {
        resultLinks.put(DATA, Lists.newArrayList(
                new ResultLink(
                        GET_BILL_HEADER_DATA_TITLE
                        , ServiceURLConstants.GET_BILL_HEAD_DATA_URL + "/" + billMetaId
                        + "/" + URL_ARGS_TOKEN_ID + "/" + billTokenId
                        , Lists.newArrayList(RequestMethodEnum.GET)
                ))
        );
        resultLinks.put(META, Lists.newArrayList(
                new ResultLink(
                        metaViewInfoDTO.getMetaName()
                        , ServiceURLConstants.GET_META_URL + "?" + MemorandumConstants.META_ID
                        + "=" + metaViewInfoDTO.getMasterDataMetaId()
                        , Lists.newArrayList(RequestMethodEnum.GET)
                ))
        );
        resultLinks.put(EDIT_BUTTON, Lists.newArrayList(
                new ResultLink(
                        EDIT_BUTTON_TITLE
                        , ServiceURLConstants.BILL_CREATE_OR_EDIT_INDEX_URL
                        + "/" + URL_ARGS_BILL_META_ID + "/" + billMetaId
                        + "/" + URL_ARGS_BILL_OPERATION + "/" + BillOperationEnum.EDIT_BILL.name()
                        + "?" + URL_ARGS_HUMP_BILL_TOKEN_ID + "=" + billTokenId
                        , Lists.newArrayList(RequestMethodEnum.GET)
                ))
        );
    }

    /**
     * 构建关联备查账的URL信息 (新建单据关联、关联备查账)
     */
    private void buildReferenceMemorandvn(MetaViewInfoDTO metaViewInfoDTO
            , Map<String, List<ResultLink>> resultLinks, String billTokenId) {
        resultLinks.put(DATA, Lists.newArrayList(
                new ResultLink(
                        GET_ENTRY_OUTLINE_VIEW_TITLE
                        , ServiceURLConstants.BILL_ENTRY_OPERATION_URL
                        + "/" + URL_ARGS_BILL_META_ID + "/" + this.billMetaId
                        + "/" + URL_ARGS_MEMORANDVN_META_ID + "/" + metaViewInfoDTO.getMasterDataMetaId()
                        + "?" + URL_ARGS_HUMP_BILL_TOKEN_ID + "=" + billTokenId
                        , Lists.newArrayList(RequestMethodEnum.GET)
                ))
        );
        resultLinks.put(META, Lists.newArrayList(
                new ResultLink(
                        metaViewInfoDTO.getMetaName()
                        , ServiceURLConstants.GET_META_URL + "?"
                        + MemorandumConstants.META_ID + "=" + metaViewInfoDTO.getViewMetaId()
                        , Lists.newArrayList(RequestMethodEnum.GET)
                ))
        );
        resultLinks.put(PREVIEW, Lists.newArrayList(
                new ResultLink(
                        MEMORANDUM_PREVIEW
                        , ServiceURLConstants.BIll_PREVIEW_INDEX_URL
                        + "/" + URL_ARGS_BILL_META_ID + "/" + metaViewInfoDTO.getRelMetaId()
                        + "/" + URL_ARGS_BILL_HEADER_TOKEN_ID + "/" + URL_NEED_INJECT_ARGS_BILL_TOKEN_ID
                        , Lists.newArrayList(RequestMethodEnum.GET)
                ))
        );
        // 如果是备查账引用单据  构造一览、建立关系、解除关系URL
        if (MetaRelEnum.MEMORANDVN.equals(metaViewInfoDTO.getMetaRelEnum())) {
            resultLinks.put(OUTLINE, Lists.newArrayList(
                    new ResultLink(
                            OUTLINE_TITLE
                            , ServiceURLConstants.MEMORANDUM_OUTLINE_URL + "/" + metaViewInfoDTO.getMasterDataMetaId()
                            + "?" + URL_ARGS_PAGE_NUMBER + "=" + URL_NEED_INJECT_ARGS_PAGE_NUMBER
                            + "&" + URL_ARGS_PAGE_SIZE + "=" + URL_NEED_INJECT_ARGS_PAGE_SIZE
                            , Lists.newArrayList(RequestMethodEnum.GET)
                    ))
            );
            resultLinks.put(BILL_REFERENCE_MEMORANDUM,
                    Lists.newArrayList(
                            new ResultLink(
                                    BILL_REFERENCE_MEMORANDUM_TITLE
                                    , ServiceURLConstants.BILL_RELATED_MEMORANDUM_OPERATION_URL + "/" + billMetaId
                                    + "/" + URL_ARGS_MEMORANDVN_META_ID + "/" + metaViewInfoDTO.getMasterDataMetaId()
                                    + "?" + URL_ARGS_HUMP_BILL_TOKEN_ID + "=" + billTokenId
                                    , Lists.newArrayList(RequestMethodEnum.POST)
                            ))
            );
            resultLinks.put(RELIEVE_BILL_REFERENCE_MEMORANDUM,
                    Lists.newArrayList(
                            new ResultLink(
                                    RELIEVE_BILL_REFERENCE_MEMORANDUM_TITLE
                                    , ServiceURLConstants.BILL_RELATED_MEMORANDUM_OPERATION_URL + "/" + billMetaId
                                    + "/" + URL_ARGS_MEMORANDVN_META_ID + "/" + metaViewInfoDTO.getMasterDataMetaId()
                                    + "?" + URL_ARGS_HUMP_BILL_TOKEN_ID + "=" + billTokenId
                                    + "&" + URL_ARGS_BILL_TOKEN_ID + "=" + URL_NEED_INJECT_ARGS_TOKEN_ID
                                    , Lists.newArrayList(RequestMethodEnum.DELETE)
                            ))
            );
        } else {
            resultLinks.put(CREATE_RELEVANCE_BILL_INDEX, Lists.newArrayList(
                    new ResultLink(
                            CREATE_RELEVANCE_BILL_INDEX_TITLE
                            , ServiceURLConstants.CREATE_ENTRY_INDEX_URL
                            + "/" + URL_ARGS_BILL_META_ID + "/" + billMetaId
                            + "/" + URL_ARGS_BILL_HEADER_TOKEN_ID + "/" + billTokenId
                            + "/" + URL_ARGS_META_ID + "/" + metaViewInfoDTO.getMasterDataMetaId()
                            , Lists.newArrayList(RequestMethodEnum.GET)
                    ))
            );
            resultLinks.put(RELIEVE_BILL_REFERENCE_MEMORANDUM,
                    Lists.newArrayList(
                            new ResultLink(
                                    RELIEVE_BILL_REFERENCE_MEMORANDUM_TITLE
                                    , ServiceURLConstants.RELIEVE_BILL_RELEVANCE_RELATION +"/"+billMetaId
                                    + "/" + URL_ARGS_MEMORANDVN_META_ID+"/"+metaViewInfoDTO.getMasterDataMetaId()
                                    + "?" + URL_ARGS_HUMP_BILL_TOKEN_ID + "=" +billTokenId
                                    +"&" + URL_ARGS_BILL_TOKEN_ID + "=" + URL_NEED_INJECT_ARGS_TOKEN_ID
                                    , Lists.newArrayList(RequestMethodEnum.DELETE)
                            ))
            );
        }
    }

}
