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
 * 创建或编辑表单时索引信息
 * Created by JiangHao on 2019/1/24.
 */
public class CreateOrEditMemorandumIndexLinkModel extends BaseResultLinkInfoModel {

    private String billMetaId;  // 表头metaId
    private String billTokenId; // 表头tokenId
    private MetaAuthRelationDTO metaAuthRelationDTO; // 根据权限查询表头meta所能查看的分录、备查账meta
    private BillOperationEnum billOperation;  // 单据操作类型

    public CreateOrEditMemorandumIndexLinkModel(String billMetaId
            , String billTokenId
            , BillOperationEnum billOperation
            , MetaAuthRelationDTO metaAuthRelationDTO
    ) {
        this.billTokenId = billTokenId;
        this.billMetaId = billMetaId;
        this.billOperation = billOperation;
        this.metaAuthRelationDTO = metaAuthRelationDTO;
        buildAllResultLinks();
        super.resultContext = new ResultContext(
                super.resultLinks
        );
    }

    /**
     * 构建全部link的信息
     */
    private void buildAllResultLinks() {
        this.resultLinks = new ArrayList<>();
        for (MetaViewInfoDTO metaViewInfoDTO : this.metaAuthRelationDTO.getRelMetaId()) {
            switch (billOperation) {
                case CREATE_NEW_BILL:
                    buildCreateNewBillURL(metaViewInfoDTO);
                    break;
                case EDIT_BILL:
                    buildEditBillURL(metaViewInfoDTO);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 构建新建表单URL信息
     */
    private void buildCreateNewBillURL(MetaViewInfoDTO metaViewInfoDTO) {
        if (MetaRelEnum.BILL_HEADER.equals(metaViewInfoDTO.getMetaRelEnum())) {
            this.resultLinks.add(
                    new ResultUserControl(
                            metaViewInfoDTO.getMetaName()
                            , CREATE_USER_CONTROL + metaViewInfoDTO.getUsercontrol()
                            , ServiceURLConstants.BILL_CREATE_OR_EDIT_DETAIL_URL
                            + "/" + URL_ARGS_BILL_META_ID + "/" + this.billMetaId
                            + "/" + URL_ARGS_META_ID + "/" + metaViewInfoDTO.getMasterDataMetaId()
                            + "/" + URL_ARGS_BILL_OPERATION + "/" + billOperation.name()
                    )
            );
        } else if (MetaRelEnum.ENTRY.equals(metaViewInfoDTO.getMetaRelEnum())) {
            this.resultLinks.add(
                    new ResultUserControl(
                            metaViewInfoDTO.getMetaName()
                            , CREATE_USER_CONTROL + metaViewInfoDTO.getUsercontrol()
                            , ServiceURLConstants.BILL_CREATE_OR_EDIT_DETAIL_URL
                            + "/" + URL_ARGS_BILL_META_ID + "/" + this.billMetaId
                            + "/" + URL_ARGS_META_ID + "/" + metaViewInfoDTO.getMasterDataMetaId()
                            + "/" + URL_ARGS_BILL_OPERATION + "/" + billOperation.name()
                            + "?" + URL_ARGS_HUMP_BILL_TOKEN_ID + "=" + URL_NEED_INJECT_ARGS_BILL_TOKEN_ID
                    )
            );
        }
    }

    /**
     * 构建编辑表单URL信息
     */
    private void buildEditBillURL(MetaViewInfoDTO metaViewInfoDTO) {
        if (MetaRelEnum.BILL_HEADER.equals(metaViewInfoDTO.getMetaRelEnum())) {
            this.resultLinks.add(
                    new ResultUserControl(
                            metaViewInfoDTO.getMetaName()
                            , EDIT_USER_CONTROL + metaViewInfoDTO.getUsercontrol()
                            , ServiceURLConstants.BILL_CREATE_OR_EDIT_DETAIL_URL
                            + "/" + URL_ARGS_BILL_META_ID + "/" + this.billMetaId
                            + "/" + URL_ARGS_META_ID + "/" + metaViewInfoDTO.getMasterDataMetaId()
                            + "/" + URL_ARGS_BILL_OPERATION + "/" + billOperation.name()
                            + "?" + URL_ARGS_HUMP_BILL_TOKEN_ID + "=" + billTokenId
                    )
            );
        } else if (MetaRelEnum.ENTRY.equals(metaViewInfoDTO.getMetaRelEnum())) {
            this.resultLinks.add(
                    new ResultUserControl(
                            metaViewInfoDTO.getMetaName()
                            , EDIT_USER_CONTROL + metaViewInfoDTO.getUsercontrol()
                            , ServiceURLConstants.BILL_CREATE_OR_EDIT_DETAIL_URL
                            + "/" + URL_ARGS_BILL_META_ID + "/" + this.billMetaId
                            + "/" + URL_ARGS_META_ID + "/" + metaViewInfoDTO.getMasterDataMetaId()
                            + "/" + URL_ARGS_BILL_OPERATION + "/" + billOperation.name()
                            + "?" + URL_ARGS_HUMP_BILL_TOKEN_ID + "=" + billTokenId
                    )
            );
        }
    }

}



