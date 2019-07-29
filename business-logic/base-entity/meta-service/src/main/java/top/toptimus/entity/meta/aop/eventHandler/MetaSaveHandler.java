package top.toptimus.entity.meta.aop.eventHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.meta.*;
import top.toptimus.repository.meta.FKeyRepository;
import top.toptimus.repository.meta.RWpermissionRepositroy;
import top.toptimus.repository.meta.RalvalueRepository;
import top.toptimus.repository.meta.TokenMetaInformationRepository;

import java.util.List;

/**
 * meta保存Handler
 *
 * @author gaoyu
 * @since 2018-6-21
 */
@Component
public class MetaSaveHandler {

    /**
     * repository
     */
    @Autowired
    private RalvalueRepository ralValueRepository;
    @Autowired
    private RWpermissionRepositroy rwPermissionRepository;
    @Autowired
    private TokenMetaInformationRepository tokenMetaInformationRepository;
    @Autowired
    private FKeyRepository fKeyRepository;

    /**
     * 保存meta
     *
     * @param tokenMetaInfoDTO tokenMetaInfoDTO
     * @param fKeyDaos         meta下所有的fkey
     * @param rWpermissionDaos meta下所有fkey的可见和读写属性
     * @param ralValueDaos     meta下fkey的ralValue  部分有
     */
    public void saveMeta(
            TokenMetaInfoDTO tokenMetaInfoDTO
            , List<FKeyDto> fKeyDaos
            , List<RWpermissionDto> rWpermissionDaos
            , List<RalValueDto> ralValueDaos
    ) {
        //  保存meta信息
        tokenMetaInformationRepository.save(
                new TokenMetaInformationDto(
                        tokenMetaInfoDTO.getTokenMetaId()
                        , tokenMetaInfoDTO.getTokenMetaName()
                        , tokenMetaInfoDTO.getMetaType()
                ));
        //  保存key
        fKeyRepository.saveAll(fKeyDaos);
        //  保存读写属性
        rwPermissionRepository.saveAll(rWpermissionDaos);
        //  保存关联关系
        ralValueRepository.saveAll(ralValueDaos);
    }
}
