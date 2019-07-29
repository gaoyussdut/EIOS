package top.toptimus.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import top.toptimus.common.result.Result;
import top.toptimus.service.BusinessUnitService;
import top.toptimus.service.domainService.MetaService;
import top.toptimus.service.domainService.TokenService;

/**
 * 基础接口
 */
@Api(value = "凭证相关接口", tags = "凭证相关接口")
@RestController
@RequestMapping(value = "/certificate")
@Controller
public class CertificateController {

    @Autowired
    private MetaService metaService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private BusinessUnitService businessUnitService;

    /**
     * 根据组织架构Id取得其下的业务单元一览
     *
     * @param orgId 组织架构Id
     * @return Result
     */
    @ApiOperation(value = "根据组织架构Id取得其下的业务单元一览")
    @GetMapping(value = "/org-id/{orgId}")
    public Result findBusinessUnitByOrgId(@PathVariable String orgId) {
        return businessUnitService.findBusinessUnitByOrgId(orgId);
    }


    /**
     * 根据业务单元id取凭证metaId
     * @param businessUnitId  业务单元id
     */
    @ApiOperation(value = "根据业务单元id取凭证metaId")
    @GetMapping(value = "/business-unit-id/{businessUnitId}")
    public Result getCertificateMetaIdByBUID(
            @PathVariable String businessUnitId
    ) {
        return metaService.getCertificateMetaIdByBUID(businessUnitId);
    }

    /**
     * 根据凭证metaId和业务单元Id取凭证tokendata
     * @param certificateMetaId     凭证metaId
     * @param businessUnitId        业务单元id
     */
    @ApiOperation(value = "根据凭证metaId和业务单元Id取凭证tokendata")
    @GetMapping(value =  "/business-unit-id/{businessUnitId}/certificate-meta-id/{certificateMetaId}")
    public Result getCertificateMetaIdToken(
            @PathVariable String certificateMetaId
            ,@PathVariable String businessUnitId
    ) {
        return tokenService.getCertificateTokenData(certificateMetaId,businessUnitId);
    }

    /**
     * 凭证接收,返回单据的metaid和tokenId
     * @param certificateMetaId     凭证metaId
     * @param businessUnitId        业务单元id
     * @param certificateTokenId    凭证tokenId
     */
    @ApiOperation(value = "凭证接收,返回单据的metaid和tokenId")
    @GetMapping(value =  "/getTargetBill/business-unit-id/{businessUnitId}/certificate-tokenId/{certificateTokenId}/certificate-meta-id/{certificateMetaId}")
    public Result receiveCertificate(
            @PathVariable String certificateMetaId
            ,@PathVariable String certificateTokenId
            ,@PathVariable String businessUnitId
    ) {
        return businessUnitService.receiveCertificate(
                certificateMetaId
                ,certificateTokenId
                ,businessUnitId);
    }

    /**
     * 根据凭证查看源单
     * @param certificateMetaId     凭证metaId
     * @param businessUnitId        业务单元id
     * @param certificateTokenId    凭证tokenId
     */
    @ApiOperation(value = "根据凭证查看原单,返回源单的metaId和tokenId")
    @GetMapping(value =  "/getSourceBill/business-unit-id/{businessUnitId}/certificate-tokenId/{certificateTokenId}/certificate-meta-id/{certificateMetaId}")
    public Result getSourceBill(
            @PathVariable String certificateMetaId
            ,@PathVariable String certificateTokenId
            ,@PathVariable String businessUnitId
    ) {
        return businessUnitService.getSourceBill(
                certificateMetaId
                ,certificateTokenId
                ,businessUnitId);
    }
}
