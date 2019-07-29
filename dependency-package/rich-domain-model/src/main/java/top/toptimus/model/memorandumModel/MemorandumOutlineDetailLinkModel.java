package top.toptimus.model.memorandumModel;

import com.google.common.collect.Lists;
import top.toptimus.common.enums.BillOperationEnum;
import top.toptimus.common.enums.RequestMethodEnum;
import top.toptimus.common.enums.UserControlEnum;
import top.toptimus.common.result.ResultContext;
import top.toptimus.common.result.ResultLink;
import top.toptimus.common.result.ResultUserControl;
import top.toptimus.common.search.SearchItem;
import top.toptimus.constantConfig.MemorandumConstants;
import top.toptimus.constantConfig.ServiceURLConstants;
import top.toptimus.meta.MetaRelation.MetaRelationDTO;
import top.toptimus.resultModel.BaseResultLinkInfoModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 备查账一览的详细link信息
 * Created by JiangHao on 2019/1/8.
 */
public class MemorandumOutlineDetailLinkModel extends BaseResultLinkInfoModel {

    // 备查账URL简介
    private static final String MEMORANDUM_OUTLINE_VIEW = "备查账一览";
    // 备查账的tab页
    private static final String MEMORANDUM_TABS = "tabs";
    // 预览备查账
    private static final String MEMORANDUM_PREVIEW = "预览备查账";
    // 预览
    private static final String PREVIEW = "preview";
    // 添加一条备查账
    private static final String ADD_MEMORANDUM = "addButton";
    // 新建单据
    private static final String NEW_BILL_INFO = "新建单据";
    // meta的title
    private static final String META_TITLE = "meta信息";
    // 获取一览数据的title
    private static final String GET_OUTLINE_DATA_TITLE = "获取一览数据";

    private List<SearchItem> searchItems;  // 当前metaId下的所有type页信息
    private String selfDefiningMeta;    //  用户自定义meta
    private MetaRelationDTO metaRelationDTO; // 单据内meta关系

    /**
     * @param metaId           meta id
     * @param searchItems      筛选项
     * @param selfDefiningMeta 用户自定义meta
     */
    public MemorandumOutlineDetailLinkModel(
            String metaId
            , MetaRelationDTO metaRelationDTO
            , List<SearchItem> searchItems
            , String selfDefiningMeta
    ) {
        super.metaId = metaId;
        this.searchItems = searchItems;
        this.selfDefiningMeta = selfDefiningMeta;
        this.metaRelationDTO = metaRelationDTO;
        //  构建最终要的返回的ResultContext的信息
        this.buildAllResultLinks();
        this.buildMetaInfoResultLinks();
        super.resultContext = new ResultContext(super.resultLinks
        );
    }

    /**
     * 构建全部link的信息
     */
    private void buildAllResultLinks() {
        this.resultLinks = new ArrayList<>();
        //  构造备查账数据一览内的tab页的URL信息
        List<ResultLink> tabLinkInfo = buildTabLinkInfo(super.metaId, this.searchItems);
        // meta信息的link
        ResultLink metaInfoResultLinks = buildMetaInfoResultLinks();
        // 添加全部的link信息
        this.resultLinks.add(
                new ResultUserControl(
                        MEMORANDUM_OUTLINE_VIEW
                        , OUTLINE_USER_CONTROL + UserControlEnum.MEMORANDUM_LIST_VIEW
                        , super.metaId
                        , new HashMap<String, List<ResultLink>>() {
                    private static final long serialVersionUID = -9048906441096754819L;

                    {
                        put(DATA, Lists.newArrayList(
                                new ResultLink(
                                        GET_OUTLINE_DATA_TITLE
                                        , ServiceURLConstants.MEMORANDUM_OUTLINE_URL + "/" + metaId
                                        + "?" + URL_ARGS_PAGE_NUMBER + "=" + URL_NEED_INJECT_ARGS_PAGE_NUMBER
                                        + "&" + URL_ARGS_PAGE_SIZE + "=" + URL_NEED_INJECT_ARGS_PAGE_SIZE
                                        , Lists.newArrayList(RequestMethodEnum.GET)
                                ))
                        );
                        put(META, Lists.newArrayList(metaInfoResultLinks));
                        put(MEMORANDUM_TABS, tabLinkInfo);
                        // 构造备查账的共同URL信息 --获取单条预览URL
                        put(PREVIEW, Lists.newArrayList(
                                new ResultLink(
                                        MEMORANDUM_PREVIEW
                                        , ServiceURLConstants.BIll_PREVIEW_INDEX_URL
                                        + "/" + URL_ARGS_BILL_META_ID + "/" + metaRelationDTO.getMasterMetaId()
                                        + "/" + URL_ARGS_BILL_HEADER_TOKEN_ID + "/" + URL_NEED_INJECT_ARGS_BILL_TOKEN_ID
                                        , Lists.newArrayList(RequestMethodEnum.GET)
                                ))
                        );
                        // 构造新增备查账的URL信息 -- 获取新建表单URL
                        put(ADD_MEMORANDUM, Lists.newArrayList(
                                new ResultLink(
                                        NEW_BILL_INFO
                                        , ServiceURLConstants.BILL_CREATE_OR_EDIT_INDEX_URL
                                        + "/" + URL_ARGS_BILL_META_ID + "/" + metaRelationDTO.getMasterMetaId()
                                        + "/" + URL_ARGS_BILL_OPERATION + "/" + BillOperationEnum.CREATE_NEW_BILL.name()
                                        , Lists.newArrayList(RequestMethodEnum.GET)
                                ))
                        );
                    }
                }
                )
        );
    }

    /**
     * 构建备查账一览中的tab页link信息
     */
    private List<ResultLink> buildTabLinkInfo(String metaId, List<SearchItem> searchItems) {
        return new ArrayList<ResultLink>() {
            private static final long serialVersionUID = 3756377131393587589L;

            {
                for (SearchItem searchItem : searchItems) {
                    add(
                            new ResultLink(
                                    searchItem.getSelectType()
                                    , ServiceURLConstants.MEMORANDUM_OUTLINE_URL
                                    + "/" + URL_ARGS_META_ID + "/" + metaId + "?"
                                    + MemorandumConstants.SELECT_TYPE + "=" + searchItem.getSelectType()
                                    + "&" + URL_ARGS_PAGE_NUMBER + "=" + URL_NEED_INJECT_ARGS_PAGE_NUMBER
                                    + "&" + URL_ARGS_PAGE_SIZE + "=" + URL_NEED_INJECT_ARGS_PAGE_SIZE
                                    , Lists.newArrayList(RequestMethodEnum.GET)
                            )
                    );
                }
            }
        };
    }

    /**
     * 构建meta的信息
     */
    private ResultLink buildMetaInfoResultLinks() {
        return new ResultLink(
                META_TITLE
                , ServiceURLConstants.GET_META_URL + "?" + MemorandumConstants.META_ID
                + "=" + this.selfDefiningMeta
                , Lists.newArrayList(RequestMethodEnum.GET)
        );
    }
}
