package top.toptimus.entity.meta.query;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.businessmodel.memorandvn.dto.CertificateDTO;
import top.toptimus.common.CurrentPage;
import top.toptimus.common.enums.process.MetaRelEnum;
import top.toptimus.constantConfig.Constants;
import top.toptimus.dao.meta.MasterBillMetaRelationDao;
import top.toptimus.exception.TopErrorCode;
import top.toptimus.exception.TopException;
import top.toptimus.meta.*;
import top.toptimus.meta.MetaRelation.MetaRelationDTO;
import top.toptimus.meta.metaFkey.MetaFKeyDTO;
import top.toptimus.meta.metaFkey.MetaFKeyFacadeDTO;
import top.toptimus.meta.metaview.MetaInfoDTO;
import top.toptimus.meta.metaview.MetaInfoFacadeDTO;
import top.toptimus.meta.relation.MetaRelDTO;
import top.toptimus.meta.signGroup.SignGroupDTO;
import top.toptimus.model.meta.query.TokenMetaInfoModel;
import top.toptimus.repository.meta.*;
import top.toptimus.repository.meta.MetaRelation.MasterBillMetaRelationRepository;
import top.toptimus.repository.meta.MetaRelation.MetaRelationRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * meta query facade
 *
 * @author lzs
 * @since 2018-6-21
 */
@Component
public class MetaQueryFacadeEntity {

    /**
     * repository
     */
    @Autowired
    private FKeyTypeRepository fKeyTypeRepository;
    @Autowired
    private FKeyRepository fKeyRepository;
    @Autowired
    private MetaInfoRepository metaInfoRepository;
    @Autowired
    private FKeyCaptionRepository fKeyCaptionRepository;
    @Autowired
    private TokenMetaInformationRepository tokenMetaInformationRepository;
    @Autowired
    private RalvalueRepository ralValueRepository;
    @Autowired
    private RWpermissionRepositroy rwPermissionRepository;
    @Autowired
    private MetaRelationRepository metaRelationRepository;
    @Autowired
    private MasterBillMetaRelationRepository masterBillMetaRelationRepository;
    @Autowired
    private SignGroupRepository signGroupRepository;
    @Autowired
    private CertificateRepository certificateRepository;
    @Autowired
    private BillEntryMetaRepository billEntryMetaRepository;

    /**
     * 取meta下fkey一览
     *
     * @param metaId metaId
     * @return fKeyDaos
     */
    public MetaFKeyFacadeDTO findCaptionAndKeyByMetaId(String metaId, boolean isReadonly) {
        List<MetaFKeyDTO> metaFKeyDTOS = new ArrayList<>();
        List<FKeyDto> fKeyDaoList = fKeyRepository.findByMetaId(metaId);
        //遍历fKeyDaoList,获取FkeyType为非基础类型的对象的fKey
        for (FKeyDto fKeyDao : fKeyDaoList) {
            if (!(fKeyDao.getFkeyType().equals(Constants.selectFkeyType)
                    || fKeyDao.getFkeyType().equals(Constants.selectInternFkeyType))) {   //  TODO    不单单这俩类型要被清洗
                metaFKeyDTOS.add(fKeyDao.buildMetaFKeyDTO());
            }
        }
        return new MetaFKeyFacadeDTO(isReadonly, metaId, metaFKeyDTOS);
    }

    //-------------------------------------------------Fkey维护----------------------------------------------

    /**
     * 查询Fkey（根据type查询，如果type为空的场合查询所有Fkey）
     *
     * @param type     key的类型
     * @param pageNo   页码
     * @param pageSize 页宽
     * @return meta中的key定义
     */
    public CurrentPage<FKeyTypeDto> findAllFkeys(String type, int pageNo, int pageSize) {
        if (StringUtils.isNotEmpty(type)) {
            return fKeyTypeRepository.findByType(type, pageNo, pageSize);
        }
        return fKeyTypeRepository.findAllFkeys(pageNo, pageSize);
    }


    /**
     * 根据metaid取得相关的Fkey
     *
     * @param metaId meta id
     * @return key列表
     */
    public List<FKeyDto> findFkeyByMetaId(String metaId) {
        return fKeyRepository.findByMetaId(metaId);
    }


    /**
     * 取meta编辑画面
     *
     * @param metaId metaId
     * @return metaViewDTOs
     */
    public List<MetaInfoDTO> findByMetaId(String metaId) {
        return metaInfoRepository.findByMetaId(metaId);
    }

    /**
     * 取meta编辑画面
     *
     * @param metaId metaId
     * @return metaViewDTOs
     */
    public MetaInfoFacadeDTO findMetaInfoFacadeDTOByMetaId(String metaId) {
        return new MetaInfoFacadeDTO(metaId, tokenMetaInformationRepository.getMetaTypeByMetaId(metaId), this.findByMetaId(metaId));
    }

    /**
     * 查找全部的key的默认caption
     *
     * @return List<FKeyCaptionDao>
     */
    public List<FKeyCaptionDto> findAllKeyCaption() {
        return fKeyCaptionRepository.findAll();
    }

    /**
     * 根据metaId查询 tokenMetaInformationDao
     * fKeyTypeDaos
     * rWpermissionDaos
     * ralValueDaos
     * 并且根据查询结果构造 TokenMetaInfo
     * 并返回 TokenMetaInfo
     *
     * @param metaId meta id
     * @return meta信息
     */
    public TokenMetaInfoDTO getMetaInfo(String metaId) {
        //根据 metaId 查询 tokenMetaInformationDao
        TokenMetaInformationDto tokenMetaInformationDto = tokenMetaInformationRepository.findByTokenMetaId(metaId);
        if (null == tokenMetaInformationDto) {
            throw new TopException(TopErrorCode.META_NOT_EXIST);// meta不存在
        }
        // 遍历 tokenMetaInformationDao 的 FKeyDaos 获取 fkey 列表
        List<FKeyDto> fKeyDaoList = fKeyRepository.findByMetaId(metaId);
        if (null == fKeyDaoList) {
            throw new TopException(TopErrorCode.META_FKEY_NOT_EXIST);// meta下fkey不存在
        }
        // 根据 metaId 查询 rWpermissionDaos
        List<RWpermissionDto> rWpermissionDaos = rwPermissionRepository.findAllByTokenMetaId(tokenMetaInformationDto.getTokenMetaId());
        if (null == rWpermissionDaos) {
            throw new TopException(TopErrorCode.META_FKEY_PROPERTY_NOT_EXIST);// meta下读写属性不存在
        }
        // 根据 metaId 查询 ralValueDaos
        List<RalValueDto> ralValueDaos = ralValueRepository.findAllByTokenMetaId(tokenMetaInformationDto.getTokenMetaId());
        // 根据
        // tokenMetaInformationDao
        // fKeyTypeDaos
        // rWpermissionDaos
        // ralValueDaos
        // 构造并返回 TokenMetaInfo
        return new TokenMetaInfoModel(tokenMetaInformationDto, fKeyDaoList, rWpermissionDaos, ralValueDaos).getTokenMetaInfoDTO();
    }

    /**
     * 根据key查找对应的默认caption
     *
     * @return List<FKeyCaptionDao>
     */
    public List<FKeyCaptionDto> findAllKeyCaptionByKeys(List<String> keys) {
        return fKeyCaptionRepository.findAllByKey(keys);
    }

    public List<MetaInfoDTO> findByMetaIds(List<String> metaIds) {
        return metaInfoRepository.findByMetaIds(metaIds);
    }

    /**
     * 根据备查帐查询单据
     *
     * @param masterMemorandvnMetaId 备查账metaId
     * @return List<MetaRelationDTO>
     */
    public List<MetaRelationDTO> findByMasterMemorandvnMetaId(String masterMemorandvnMetaId) {
        return new ArrayList<MetaRelationDTO>() {
            private static final long serialVersionUID = -2956491356731353203L;

            {
                metaRelationRepository.findByMasterMemorandvnMetaId(masterMemorandvnMetaId)
                        .forEach(metaRelationDao -> add(metaRelationDao.build()));
            }
        };
    }

    /**
     * 根据表头meta获取下级meta
     *
     * @param billMetaId  表头meta id
     * @param metaRelEnum 表单meta关联类型
     */
    public List<String> findEntryMetaByBillMeta(String billMetaId, MetaRelEnum metaRelEnum) {
        List<String> entryMetaIds = new ArrayList<>();
        MasterBillMetaRelationDao masterBillMetaRelationDao = masterBillMetaRelationRepository.findByBillMasterMetaId(billMetaId);
        masterBillMetaRelationDao.getMasterMetaInfoDTOS().forEach(masterMetaInfoDTO -> {
            if (masterMetaInfoDTO.getMetaRelEnum().equals(metaRelEnum)) {
                entryMetaIds.add(masterMetaInfoDTO.getEntryMasterMetaId());
            }
        });
        entryMetaIds.add(billMetaId);
        return entryMetaIds;
    }

    /**
     * 根据metaIds获取metaId的定义信息
     *
     * @param metaIds metaIds
     * @return List<TokenMetaInformationDto>
     */
    public List<TokenMetaInformationDto> findMetaFormationsByMetaIds(List<String> metaIds) {
        return tokenMetaInformationRepository.findByTokenMetaIds(metaIds);
    }

    /**
     * 根据metaId查询SignGroupDTO信息
     *
     * @param metaId 单据metaId
     * @return 汇签组信息
     */
    public SignGroupDTO findBySignMetaId(String metaId) {
        return signGroupRepository.findBySignMetaId(metaId);
    }

    /**
     * 根据业务单元id取凭证metaId
     *
     * @param businessUnitId 业务单元id
     * @return List<CertificateDTO> 凭证信息
     */
    public List<CertificateDTO> getCertificateMetaIdByBUID(String businessUnitId) {
        return certificateRepository.getCertificateMetaIdByBUID(businessUnitId);
    }

    /**
     * 根据业务单元id和指定的凭证metaId取未接收的凭证tokenIds
     *
     * @param businessUnitId    业务单元id
     * @param certificateMetaId 凭证metaId
     * @return List<String> 凭证tokenIds
     */
    public List<String> getCertificateTokenIds(String businessUnitId, String certificateMetaId) {
        return certificateRepository.getCertificateTokenIds(businessUnitId, certificateMetaId);
    }

    /**
     * 获取分录meta
     *
     * @param billMetaId 表头meta
     */
    public List<TokenMetaInformationDto> getEntryMetas(String billMetaId) {
        return billEntryMetaRepository.findTokenMetaInfoByBillMetaId(billMetaId);
    }

    /**
     * 根据fkey查看Constant
     *
     * @param fkey fkey
     * @return String 内容
     */
    public String getConstantByKey(String fkey) {
        return fKeyRepository.getConstantByKey(fkey);
    }

    /**
     * 获取关联单据meta
     * @param billMetaId
     * @return
     */
    public List<MetaRelDTO> getRelMetasByTokenTemplateId(String billMetaId){
        return billEntryMetaRepository.getRelMetasByBillMeta(billMetaId);
    }

}
