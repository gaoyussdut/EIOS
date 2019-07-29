package top.toptimus.resultModel;

import lombok.Getter;
import top.toptimus.common.result.ResultContext;
import top.toptimus.common.result.ResultUserControl;

import java.util.List;

public abstract class BaseResultLinkInfoModel {

    // 数据
    protected static final String DATA = "data";
    // meta
    protected static final String META = "meta";
    // metaId
    protected static final String META_ID = "metaId";
    // URl中的参数表头metaId
    protected static final String URL_ARGS_BILL_META_ID = "bill-meta-id";
    // URl中的参数分录metaId
    protected static final String URL_ARGS_BILL_ENTRY_META_ID = "entry-meta-id";
    // URl中的参数metaId
    protected static final String URL_ARGS_META_ID = "meta-id";
    // URl中的参数表头metaId
    protected static final String URL_ARGS_BILL_HEADER_TOKEN_ID = "bill-token-id";
    // URl中的参数表头metaId
    protected static final String URL_ARGS_HUMP_BILL_META_ID = "billMetaId";
    // URl中的参数表头tokenId
    protected static final String URL_ARGS_HUMP_BILL_TOKEN_ID = "billTokenId";
    // URl中的参数分录tokenId
    protected static final String URL_ARGS_BILL_ENTRY_TOKEN_ID = "entryTokenId";
    // URL中的参数tokenId
    protected static final String URL_ARGS_BILL_TOKEN_ID = "tokenId";
    // URL中的参数tokenId
    protected static final String URL_ARGS_TOKEN_ID = "token-id";
    // URL中需要注入参数tokenId
    protected static final String URL_NEED_INJECT_ARGS_TOKEN_ID = "{{tokenId}}";
    // URL中需要注入参数表头tokenId
    protected static final String URL_NEED_INJECT_ARGS_BILL_TOKEN_ID = "{{billTokenId}}";
    // 单据操作类型
    protected static final String URL_ARGS_BILL_OPERATION = "bill-operation";
    // URL中参数页码
    protected static final String URL_ARGS_PAGE_NUMBER = "pageNumber";
    // URL中需要注入的参数页码
    protected static final String URL_NEED_INJECT_ARGS_PAGE_NUMBER = "{{pageNumber}}";
    // URL中参数页码
    protected static final String URL_ARGS_PAGE_SIZE = "pageSize";
    // URL中需要注入的参数页码
    protected static final String URL_NEED_INJECT_ARGS_PAGE_SIZE = "{{pageSize}}";
    // URL中的参数备查账metaId
    protected static final String URL_ARGS_MEMORANDVN_META_ID = "memorandvn-meta-id";
    // 用户控件的类别细化区分  新建状态下
    protected static final String CREATE_USER_CONTROL = "CREATE_";
    // 用户控件的类别细化区分  编辑状态下
    protected static final String EDIT_USER_CONTROL = "EDIT_";
    // 用户控件的类别细化区分  预览状态下
    protected static final String PREVIEW_USER_CONTROL = "PREVIEW_";
    // 用户控件的类别细化区分  一览状态下
    protected static final String OUTLINE_USER_CONTROL = "OUTLINE_";


    protected String metaId;   // 当前的基础资料备查账的metaId
    protected Object data; // 数据的信息
    @Getter
    protected ResultContext resultContext;  // 最终返回给前端的返回值
    protected List<ResultUserControl> resultLinks;   //  全部链接列表

}
