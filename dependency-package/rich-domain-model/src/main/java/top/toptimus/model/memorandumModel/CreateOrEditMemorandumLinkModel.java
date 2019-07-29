package top.toptimus.model.memorandumModel;

import com.google.common.collect.Lists;
import top.toptimus.common.enums.BillOperationEnum;
import top.toptimus.common.enums.RequestMethodEnum;
import top.toptimus.common.enums.UserControlEnum;
import top.toptimus.common.result.ResultContext;
import top.toptimus.common.result.ResultLink;
import top.toptimus.common.result.ResultUserControl;
import top.toptimus.constantConfig.ServiceURLConstants;
import top.toptimus.meta.MetaRelation.*;
import top.toptimus.resultModel.BaseResultLinkInfoModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 创建或编辑表单时URL详细信息
 * Created by JiangHao on 2019/1/22.
 */
public class CreateOrEditMemorandumLinkModel extends BaseResultLinkInfoModel {

    // 获取分录数据的title
    private static final String GET_ENTRY_DATA_TITLE = "获取分录数据";
    // 提交分录数据
    private static final String SUBMIT_ENTRY_DATA = "submitEntry";
    // 提交分录数据title
    private static final String SUBMIT_ENTRY_DATA_TITLE = "提交分录数据";
    // 提交表头数据
    private static final String SUBMIT_BILL_DATA = "submitBill";
    // 提交表头数据title
    private static final String SUBMIT_BILL_DATA_TITLE = "提交表头数据";
    // 删除分录数据
    private static final String DELETE_ENTRY = "deleteEntry";
    // 删除分录数据title
    private static final String DELETE_ENTRY_TITLE = "删除分录数据";
    // 获取新建表头数据
    private static final String GET_NEW_BILL_HEADER = "getNewBillHeader";
    // 获取新建表头数据title
    private static final String GET_NEW_BILL_HEADER_TITLE = "获取新建表头数据";
    // 获取表头数据title
    private static final String GET_BILL_DATA_TITLE = "获取表头数据";
    // 获取新建分录
    private static final String GET_NEW_ENTRY = "getNewEntry";
    // 获取新建分录数据title
    private static final String GET_NEW_ENTRY_TITLE = "获取新建分录数据";
    // 创建关联表单
    private static final String CREATE_ASSOCIATED_BILL = "createAssociatedBill";
    // 获取单条分录
    private static final String GET_SINGLE_ENTRY = "getSingleEntry";
    // 获取单条分录title
    private static final String GET_SINGLE_ENTRY_TITLE = "获取单条分录数据";
    // 创建关联表单title
    private static final String CREATE_ASSOCIATED_BILL_TITLE = "创建关联表单";
    // URl中的需要注入的参数分录tokenId
    private static final String URL_NEED_INJECT_ARGS_ENTRY_TOKEN_ID = "{{entryTokenId}}";
    // URL中参数 前置单据metaId
    private static final String URL_ARGS_PRE_META_ID = "pre-meta-id";
    // URL中参数 前置单据tokenId
    private static final String URL_ARGS_PRE_TOKEN_ID = "pre-token-id";

    private String billMetaId; // 表头metaId
    private String billTokenId; // 表头tokenId （如果传的话为编辑页面 不传的话为新建单据）
    private MetaAuthRelationDTO metaAuthRelationDTO; // 根据权限查询表头meta所能查看的分录、备查账meta
    private BillOperationEnum billOperationEnum; // 单据操作类型
    private String userControlTypePrefix; // 用户控件的前缀

    public CreateOrEditMemorandumLinkModel(
            String billMetaId
            , String billTokenId
            , String metaId
            , MetaAuthRelationDTO metaAuthRelationDTO
            , BillOperationEnum billOperationEnum
    ) {
        this.billMetaId = billMetaId;
        this.billTokenId = billTokenId;
        super.metaId = metaId;
        this.metaAuthRelationDTO = metaAuthRelationDTO;
        this.userControlTypePrefix =
                BillOperationEnum.EDIT_BILL.equals(billOperationEnum)
                        ? EDIT_USER_CONTROL : CREATE_USER_CONTROL;
        this.billOperationEnum = billOperationEnum;
        buildAllResultLinks();
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
            if (super.metaId.equals(metaViewInfoDTO.getMasterDataMetaId())
                    || super.metaId.equals(metaViewInfoDTO.getRelMetaId())) {
                // 根据表单meta关联类型 构造URL的详细信息
                switch (metaViewInfoDTO.getMetaRelEnum()) {
                    case CERTIFICATE:
                    case BILL_HEADER:
                        // 构建新建单据的表头URL信息
                        buildCreateBillHeaderLink(metaViewInfoDTO);
                        break;
                    case ENTRY:
                        // 构建新建单据分录的URL信息
                        buildCreateBillEntryLink(metaViewInfoDTO);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 构建新建单据分录的URL信息
     */
    private void buildCreateBillEntryLink(MetaViewInfoDTO metaViewInfoDTO) {
        this.resultLinks.add(
                new ResultUserControl(
                        metaViewInfoDTO.getMetaName()
                        , this.userControlTypePrefix + metaViewInfoDTO.getUsercontrol()
                        , metaViewInfoDTO.getMasterDataMetaId()
                        , new HashMap<String, List<ResultLink>>() {
                    private static final long serialVersionUID = -5233361349086742801L;

                    {
                        put(DATA, Lists.newArrayList(
                                new ResultLink(
                                        GET_ENTRY_DATA_TITLE
                                        , ServiceURLConstants.BILL_ENTRY_OPERATION_URL
                                        + "/" + URL_ARGS_BILL_HEADER_TOKEN_ID + "/" + billTokenId
                                        + "/" + URL_ARGS_BILL_ENTRY_META_ID + "/" + metaViewInfoDTO.getMasterDataMetaId()
                                        + "?" + URL_ARGS_HUMP_BILL_META_ID + "=" + billMetaId
                                        , Lists.newArrayList(RequestMethodEnum.GET)
                                ))
                        );
                        put(META, Lists.newArrayList(
                                new ResultLink(
                                        metaViewInfoDTO.getMetaName()
                                        , ServiceURLConstants.GET_META_URL + "?" + META_ID + "="
                                        + metaViewInfoDTO.getViewMetaId()
                                        , Lists.newArrayList(RequestMethodEnum.GET)
                                ))
                        );
                        put(SUBMIT_ENTRY_DATA, Lists.newArrayList(
                                new ResultLink(
                                        SUBMIT_ENTRY_DATA_TITLE
                                        , ServiceURLConstants.BILL_ENTRY_OPERATION_URL
                                        + "/" + URL_ARGS_BILL_HEADER_TOKEN_ID + "/" + billTokenId
                                        + "/" + URL_ARGS_BILL_ENTRY_META_ID + "/" + metaViewInfoDTO.getMasterDataMetaId()
                                        , Lists.newArrayList(RequestMethodEnum.POST)
                                ))
                        );
                        put(DELETE_ENTRY, Lists.newArrayList(
                                new ResultLink(
                                        DELETE_ENTRY_TITLE
                                        , ServiceURLConstants.BILL_ENTRY_OPERATION_URL
                                        + "/" + URL_ARGS_BILL_HEADER_TOKEN_ID + "/" + billTokenId
                                        + "/" + URL_ARGS_BILL_ENTRY_META_ID + "/" + metaViewInfoDTO.getMasterDataMetaId()
                                        + "?" + URL_ARGS_BILL_ENTRY_TOKEN_ID + "=" + URL_NEED_INJECT_ARGS_ENTRY_TOKEN_ID
                                        , Lists.newArrayList(RequestMethodEnum.DELETE)
                                ))
                        );
                        put(GET_NEW_ENTRY, Lists.newArrayList(
                                new ResultLink(
                                        GET_NEW_ENTRY_TITLE
                                        , ServiceURLConstants.BILL_ENTRY_OPERATION_URL
                                        + "/" + URL_ARGS_BILL_HEADER_TOKEN_ID + "/" + billTokenId
                                        + "/" + URL_ARGS_BILL_ENTRY_META_ID + "/" + metaViewInfoDTO.getMasterDataMetaId()
                                        , Lists.newArrayList(RequestMethodEnum.PUT)
                                ))
                        );
                        put(GET_SINGLE_ENTRY, Lists.newArrayList(
                                new ResultLink(
                                        GET_SINGLE_ENTRY_TITLE
                                        , ServiceURLConstants.CREATE_AND_READ_BILL_URL
                                        + "/" + metaViewInfoDTO.getMasterDataMetaId()
                                        + "/" + URL_ARGS_TOKEN_ID
                                        + "/" + URL_NEED_INJECT_ARGS_TOKEN_ID
                                        , Lists.newArrayList(RequestMethodEnum.GET)
                                )
                        ));
                    }
                }
                )
        );
    }

    /**
     * 构建新建单据的表头URL信息
     */
    private void buildCreateBillHeaderLink(MetaViewInfoDTO metaViewInfoDTO) {
        // 如果当前操作为创建关联单据 取备查账引用单据的meta  否则取正常的metaId
        String currentBillMeta = BillOperationEnum.CREATE_MEMORANDUM_RELEVANCE_BILL.equals(billOperationEnum)
                ? metaViewInfoDTO.getRelMetaId() : metaViewInfoDTO.getMasterDataMetaId();
        // 提交的表头meta
        String submitBillHeaderMetaId = BillOperationEnum.CREATE_MEMORANDUM_RELEVANCE_BILL.equals(billOperationEnum)
                ? metaViewInfoDTO.getRelMetaId() : billMetaId;
        this.resultLinks.add(
                new ResultUserControl(
                        metaViewInfoDTO.getMetaName()
                        , this.userControlTypePrefix + UserControlEnum.BILL_HEADER
                        , currentBillMeta
                        , new HashMap<String, List<ResultLink>>() {
                    private static final long serialVersionUID = 521933808287542258L;

                    {
                        // 根据单据操作类型  构造是获取表头数据  还是获取新建表头的数据 或是备查账新建单据获取数据
                        switch (billOperationEnum) {
                            case CREATE_NEW_BILL:
                                put(GET_NEW_BILL_HEADER, Lists.newArrayList(
                                        new ResultLink(
                                                GET_NEW_BILL_HEADER_TITLE
                                                , ServiceURLConstants.CREATE_AND_READ_BILL_URL + "/" + billMetaId
                                                , Lists.newArrayList(RequestMethodEnum.PUT)
                                        ))
                                );
                                put(DATA, Lists.newArrayList(
                                        new ResultLink(
                                                GET_BILL_DATA_TITLE
                                                , ServiceURLConstants.CREATE_AND_READ_BILL_URL + "/" + currentBillMeta
                                                + "/" + URL_ARGS_TOKEN_ID + "/" + URL_NEED_INJECT_ARGS_BILL_TOKEN_ID
                                                , Lists.newArrayList(RequestMethodEnum.GET)
                                        ))
                                );
                                break;
                            case EDIT_BILL:
                                put(DATA, Lists.newArrayList(
                                        new ResultLink(
                                                GET_BILL_DATA_TITLE
                                                , ServiceURLConstants.CREATE_AND_READ_BILL_URL + "/" + billMetaId
                                                + "/" + URL_ARGS_TOKEN_ID + "/" + billTokenId
                                                , Lists.newArrayList(RequestMethodEnum.GET)
                                        ))
                                );
                                break;
                            case CREATE_MEMORANDUM_RELEVANCE_BILL:
                                put(CREATE_ASSOCIATED_BILL, Lists.newArrayList(
                                        new ResultLink(
                                                CREATE_ASSOCIATED_BILL_TITLE
                                                , ServiceURLConstants.CREATE_RELEVANCE_BILL_URL
                                                + "/" + URL_ARGS_PRE_META_ID + "/" + billMetaId
                                                + "/" + URL_ARGS_PRE_TOKEN_ID + "/" + billTokenId
                                                + "/" + URL_ARGS_META_ID + "/" + metaViewInfoDTO.getRelMetaId()
                                                , Lists.newArrayList(RequestMethodEnum.PUT)
                                        ))
                                );
                                put(DATA, Lists.newArrayList(
                                        new ResultLink(
                                                GET_BILL_DATA_TITLE
                                                , ServiceURLConstants.CREATE_AND_READ_BILL_URL + "/" + currentBillMeta
                                                + "/" + URL_ARGS_TOKEN_ID + "/" + URL_NEED_INJECT_ARGS_BILL_TOKEN_ID
                                                , Lists.newArrayList(RequestMethodEnum.GET)
                                        ))
                                );
                                break;
                            default:
                                break;
                        }
                        put(META, Lists.newArrayList(
                                new ResultLink(
                                        metaViewInfoDTO.getMetaName()
                                        , ServiceURLConstants.GET_META_URL + "?" + META_ID + "="
                                        + currentBillMeta
                                        , Lists.newArrayList(RequestMethodEnum.GET)
                                ))
                        );
                        put(SUBMIT_BILL_DATA, Lists.newArrayList(
                                new ResultLink(
                                        SUBMIT_BILL_DATA_TITLE
                                        , ServiceURLConstants.MEMORANDVN_DATA_URL
                                        + "/" + URL_ARGS_META_ID + "/" + submitBillHeaderMetaId
                                        , Lists.newArrayList(RequestMethodEnum.POST)
                                ))
                        );
                    }
                }
                )
        );
    }


}
