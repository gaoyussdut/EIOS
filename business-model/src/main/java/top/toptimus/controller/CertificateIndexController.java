package top.toptimus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.toptimus.common.result.ResultContext;
import top.toptimus.model.bill.TargetBillDetailModel;
import top.toptimus.model.certificateModel.CertificateDetailModel;
import top.toptimus.model.certificateModel.CertificateListModel;
import top.toptimus.service.domainService.MetaService;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by CongChenri on 2019/4/26.
 */
@Api(value = "凭证索引接口", tags = "凭证索引管理")
@RestController
@RequestMapping(value = "/certificate")
@Controller
public class CertificateIndexController {

    @Autowired
    private MetaService metaService;

    /**
     * 根据业务单元取得凭证一览
     *
     * @param businessUnitId 业务单元
     * @return ResultContext
     */
    @ApiOperation(value = "根据业务单元取得 一览信息")
    @GetMapping(value = "/businessUnitId/{businessUnitId}")
    @SuppressWarnings("unchecked")
    public ResultContext findCertificateList(@PathVariable String businessUnitId) {
        try {
            Object data = metaService.getCertificateMetaIdByBUID(businessUnitId).getData();
            List<String> certificateMetaIds =  data== null ? null : (ArrayList<String>)data ;
            return new CertificateListModel(businessUnitId,certificateMetaIds).getResultContext();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 取得凭证详情
     *
     * @param businessUnitId     业务单元
     * @param certificateMetaId  凭证metaId
     * @param certificateTokenId 凭证TokenId
     * @return ResultContext
     */
    @ApiOperation(value = "凭证详情 索引信息")
    @GetMapping(value = "/getTargetBill/businessUnitId/{businessUnitId}/certificateMetaId/{certificateMetaId}/certificateTokenId/{certificateTokenId}")
    public ResultContext findCertificateDetail(@PathVariable String businessUnitId, @PathVariable String certificateMetaId,@PathVariable String certificateTokenId) {
        return new CertificateDetailModel(businessUnitId, certificateMetaId, certificateTokenId).getResultContext();
    }

    /**
     * 取得源单详情
     *
     * @param businessUnitId     业务单元
     * @param certificateMetaId  凭证metaId
     * @param certificateTokenId 凭证TokenId
     * @return ResultContext
     */
    @ApiOperation(value = "源单详情 索引信息")
    @GetMapping(value = "/getSourceBill/businessUnitId/{businessUnitId}/certificateMetaId/{certificateMetaId}/certificateTokenId/{certificateTokenId}")
    public ResultContext findTargetBillDetail(@PathVariable String businessUnitId, @PathVariable String certificateMetaId,@PathVariable String certificateTokenId) {

        return new TargetBillDetailModel(businessUnitId, certificateMetaId, certificateTokenId).getResultContext();
    }

}
