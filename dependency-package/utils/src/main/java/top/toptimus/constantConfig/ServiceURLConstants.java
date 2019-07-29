package top.toptimus.constantConfig;

/**
 * 服务的URL地址信息
 * Created by JiangHao on 2019/1/9.
 */
public class ServiceURLConstants {
    // 获取备查账数据URL
    public static final String MEMORANDVN_DATA_URL = "/bill/bill";
    // 取meta信息URL
    public static final String GET_META_URL = "/meta/findByMetaId";
    // 备查账一览URL
    public static final String MEMORANDUM_OUTLINE_URL = "/bill/memorandvn/meta-id";
    // 备查账一览的索引信息URL
    public static final String MEMORANDUM_OUTLINE_INDEX_URL = "/bill/outlineIndex";
    // 备查账一览的索引信息URL
    public static final String MEMORANDUM_OUTLINE_DETAIL_URL = "/bill/outlineDetail";
    // 表单创建或编辑表单索引信息
    public static final String BILL_CREATE_OR_EDIT_INDEX_URL = "/bill/createOrEditIndex";
    // 表单创建或编辑表单URL详细信息
    public static final String BILL_CREATE_OR_EDIT_DETAIL_URL = "/bill/createOrEditDetail";
    // 表单预览索引信息
    public static final String BIll_PREVIEW_INDEX_URL = "/bill/previewIndex";
    // 表单预览详细信息
    public static final String BIll_PREVIEW_DETAIL_URL = "/bill/previewDetail";
    // 创建关联表单URL
    public static final String CREATE_RELEVANCE_BILL_URL = "/bill/bill";
    // 预览表单添加关联单据、分录URL的索引信息
    public static final String CREATE_ENTRY_INDEX_URL = "/bill/createEntryIndex";
    // 表单的表头创建与读取URL
    public static final String CREATE_AND_READ_BILL_URL = "/bill/bill/meta-id";
    // 获取表头数据的URL
    public static final String GET_BILL_HEAD_DATA_URL = "/bill/bill/meta-id";
    // 单据相关的操作备查账数据URL
    public static final String BILL_RELATED_MEMORANDUM_OPERATION_URL = "/bill/entry/bill-meta-id";
    // 解除表单的关联关系
    public static final String RELIEVE_BILL_RELEVANCE_RELATION = "/bill/entry/bill-meta-id";
    // 单据的分录操作
    public static final String BILL_ENTRY_OPERATION_URL = "/bill/entry";
    // 凭证相关-业务单元
    public static final String BUSINESS_UNIT_ID_URL = "/business-unit-id/";
    // 凭证相关-目标单据
    public static final String TARGET_BILL_BUSINESS_UNIT_ID_URL = "/getTargetBill/business-unit-id/";
    // 凭证相关-源单据
    public static final String SOURCE_BILL_BUSINESS_UNIT_ID_URL = "/getSourceBill/business-unit-id/";
    // 凭证相关-凭证MetaId
    public static final String CERTIFICATE_META_ID_URL = "/certificate-meta-id/";
    // 凭证相关-凭证TokenId
    public static final String CERTIFICATE_TOKEN_ID_URL = "/certificate-tokenId/";

}
