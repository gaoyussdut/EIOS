package top.toptimus.model.certificateModel;

import top.toptimus.common.result.ResultContext;
import top.toptimus.common.result.ResultUnit;
import top.toptimus.constantConfig.ServiceURLConstants;
import top.toptimus.resultModel.BaseResultLinkInfoModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 凭证一览信息
 */
public class CertificateListModel  extends BaseResultLinkInfoModel {

    private String businessUnitId;            // 业务单元
    private List<String> certificateMetaIds;  // 凭证MetaId

    /**
     * 构造方法
     *
     * @param businessUnitId     业务单元
     * @param certificateMetaIds 凭证MetaId
     */
    public CertificateListModel(String businessUnitId , List<String> certificateMetaIds) {
        this.businessUnitId = businessUnitId;
        this.certificateMetaIds = certificateMetaIds;
        this.buildAllResultLinks();
    }

    /**
     * 构建凭证一览信息link
     */
    private void buildAllResultLinks() {
        this.resultContext = new ResultContext();
        resultContext.setResultUnits(new ArrayList<ResultUnit>(){{
            for(String certificateMetaId : certificateMetaIds) {
                add(new ResultUnit(ServiceURLConstants.GET_META_URL + "?" + META_ID + "="
                        + certificateMetaId,
                        ServiceURLConstants.BUSINESS_UNIT_ID_URL + businessUnitId + ServiceURLConstants.CERTIFICATE_META_ID_URL + certificateMetaId));
            }
        }});
    }
}
