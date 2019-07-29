package top.toptimus.model.memorandumModel;

import top.toptimus.common.enums.BillOperationEnum;
import top.toptimus.common.enums.process.MetaRelEnum;
import top.toptimus.common.result.ResultContext;
import top.toptimus.common.result.ResultUserControl;
import top.toptimus.constantConfig.ServiceURLConstants;
import top.toptimus.meta.MetaRelation.MetaAuthRelationDTO;
import top.toptimus.meta.MetaRelation.MetaViewInfoDTO;
import top.toptimus.resultModel.BaseResultLinkInfoModel;

import java.util.ArrayList;

/**
 * 备查账预览分录处理的索引信息
 * Created by JiangHao on 2019/1/29.
 */
public class MemorandumPreviewEntryHandlerIndexLinkModel extends BaseResultLinkInfoModel {

    private String billMetaId; // 表头metaId
    private String billTokenId; // 表头tokenId
    private MetaAuthRelationDTO metaAuthRelationDTO; // 根据权限查询表头meta所能查看的分录、备查账meta
    private MetaAuthRelationDTO associatMetaAuthRelationDTO; // 关联备查账的单据的信息

    public MemorandumPreviewEntryHandlerIndexLinkModel(
            String billMetaId
            , String billTokenId
            , String metaId
            , MetaAuthRelationDTO metaAuthRelationDTO
            , MetaAuthRelationDTO associatMetaAuthRelationDTO
    ) {
        this.billMetaId = billMetaId;
        this.billTokenId = billTokenId;
        super.metaId = metaId;
        this.metaAuthRelationDTO = metaAuthRelationDTO;
        this.associatMetaAuthRelationDTO = associatMetaAuthRelationDTO;
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
            if (super.metaId.equals(metaViewInfoDTO.getMasterDataMetaId())) {
                switch (metaViewInfoDTO.getMetaRelEnum()) {
                    case CERTIFICATE:
                        buildDocRelateLinks();
                        break;
                    case ENTRY:
                        buildEntryLinks(metaViewInfoDTO);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    /**
     * 构建单据新建关联备查账单据的links
     */
    private void buildDocRelateLinks() {
        for (MetaViewInfoDTO viewInfoDTO : associatMetaAuthRelationDTO.getRelMetaId()) {
            if (MetaRelEnum.BILL_HEADER.equals(viewInfoDTO.getMetaRelEnum())
                    || MetaRelEnum.ENTRY.equals(viewInfoDTO.getMetaRelEnum())) {
                String currentBillMeta = MetaRelEnum.BILL_HEADER.equals(viewInfoDTO.getMetaRelEnum())
                        ? this.billMetaId : associatMetaAuthRelationDTO.getMetaId();
                // 如果是创建备查账关联单据将前置表头token拼接 否则为需要注入的billTokenId
                String billTokenId = MetaRelEnum.ENTRY.equals(viewInfoDTO.getMetaRelEnum())
                        ? URL_NEED_INJECT_ARGS_BILL_TOKEN_ID : this.billTokenId;
                this.resultLinks.add(
                        new ResultUserControl(
                                viewInfoDTO.getMetaName()
                                , CREATE_USER_CONTROL + viewInfoDTO.getUsercontrol()
                                , ServiceURLConstants.BILL_CREATE_OR_EDIT_DETAIL_URL
                                + "/" + URL_ARGS_BILL_META_ID + "/" + currentBillMeta
                                + "/" + URL_ARGS_META_ID + "/" + viewInfoDTO.getMasterDataMetaId()
                                + "/" + URL_ARGS_BILL_OPERATION + "/" + BillOperationEnum.CREATE_MEMORANDUM_RELEVANCE_BILL.name()
                                + "?" + URL_ARGS_HUMP_BILL_TOKEN_ID + "=" + billTokenId
                        )
                );
            }
        }
    }

    /**
     * 构建单据分录links
     */
    private void buildEntryLinks(MetaViewInfoDTO metaViewInfoDTO) {
        this.resultLinks.add(
                new ResultUserControl(
                        metaViewInfoDTO.getMetaName()
                        , CREATE_USER_CONTROL + metaViewInfoDTO.getUsercontrol()
                        , ServiceURLConstants.BILL_CREATE_OR_EDIT_DETAIL_URL
                        + "/" + URL_ARGS_BILL_META_ID + "/" + this.billMetaId
                        + "/" + URL_ARGS_META_ID + "/" + metaViewInfoDTO.getMasterDataMetaId()
                        + "/" + URL_ARGS_BILL_OPERATION + "/" + BillOperationEnum.EDIT_BILL.name()
                        + "?" + URL_ARGS_HUMP_BILL_TOKEN_ID + "=" + this.billTokenId
                )
        );
    }


}
