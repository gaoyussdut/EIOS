package top.toptimus.model.bill;

import top.toptimus.common.result.ResultContext;
import top.toptimus.common.result.ResultUnit;
import top.toptimus.constantConfig.ServiceURLConstants;
import top.toptimus.resultModel.BaseResultLinkInfoModel;

import java.util.ArrayList;

/**
 * 源单详情信息
 */
public class TargetBillDetailModel extends BaseResultLinkInfoModel {

    private String businessUnitId;     // 业务单元
    private String certificateMetaId;  // 凭证MetaId
    private String certificateTokenId; // 凭证TokenId

    /**
     * 构造方法
     *
     * @param businessUnitId     业务单元
     * @param certificateMetaId  凭证MetaId
     * @param certificateTokenId 凭证TokenId
     */
    public TargetBillDetailModel(String businessUnitId , String certificateMetaId , String certificateTokenId ) {
        this.businessUnitId = businessUnitId;
        this.certificateMetaId = certificateMetaId;
        this.certificateTokenId = certificateTokenId;
        this.buildAllResultLinks();
    }

    /**
     * 构建源单详情信息link
     */
    private void buildAllResultLinks() {
        this.resultContext = new ResultContext();
        resultContext.setResultUnits(new ArrayList<ResultUnit>(){{
            add(new ResultUnit(ServiceURLConstants.GET_META_URL + "?" + META_ID + "="
                    + certificateMetaId,
                    ServiceURLConstants.SOURCE_BILL_BUSINESS_UNIT_ID_URL+businessUnitId +ServiceURLConstants.CERTIFICATE_TOKEN_ID_URL + certificateTokenId + ServiceURLConstants.CERTIFICATE_META_ID_URL + certificateMetaId));
        }});
    }
}
