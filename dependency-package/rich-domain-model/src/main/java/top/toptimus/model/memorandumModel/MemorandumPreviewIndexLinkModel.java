package top.toptimus.model.memorandumModel;

import top.toptimus.common.result.ResultContext;
import top.toptimus.common.result.ResultUserControl;
import top.toptimus.constantConfig.ServiceURLConstants;
import top.toptimus.meta.MetaRelation.MetaAuthRelationDTO;
import top.toptimus.meta.MetaRelation.MetaViewInfoDTO;
import top.toptimus.resultModel.BaseResultLinkInfoModel;

import java.util.ArrayList;

/**
 * 构建预览单据的索引信息
 * Created by JiangHao on 2019/1/23.
 */
public class MemorandumPreviewIndexLinkModel extends BaseResultLinkInfoModel {

    private MetaAuthRelationDTO metaAuthRelationDTO; // 根据权限查询表头meta所能查看的分录、备查账meta
    private String billMetaId; // 表头metaId
    private String billTokenId; // 表头token

    public MemorandumPreviewIndexLinkModel(String billTokenId
            , String billMetaId, MetaAuthRelationDTO metaAuthRelationDTO) {
        this.metaAuthRelationDTO = metaAuthRelationDTO;
        this.billMetaId = billMetaId;
        this.billTokenId = billTokenId;
        buildAllResultLinks();
        super.resultContext = new ResultContext(
                super.resultLinks
        );
    }

    /**
     * 构建备查账预览信息的全部link
     */
    private void buildAllResultLinks() {
        this.resultLinks = new ArrayList<>();
        for (MetaViewInfoDTO metaViewInfoDTO : this.metaAuthRelationDTO.getRelMetaId()) {
            this.resultLinks.add(
                    new ResultUserControl(
                            metaViewInfoDTO.getMetaName()
                            , PREVIEW_USER_CONTROL + metaViewInfoDTO.getUsercontrol()
                            , ServiceURLConstants.BIll_PREVIEW_DETAIL_URL
                            + "/" + URL_ARGS_BILL_META_ID + "/" + this.billMetaId
                            + "/" + URL_ARGS_BILL_HEADER_TOKEN_ID + "/" + this.billTokenId
                            + "/" + URL_ARGS_META_ID + "/" + metaViewInfoDTO.getMasterDataMetaId()
                    )
            );
        }

    }

}
