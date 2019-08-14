package top.toptimus.entity.tokentemplate.query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.constantConfig.Constants;
import top.toptimus.meta.FKeyDto;
import top.toptimus.meta.metaFkey.MetaFKeyDTO;
import top.toptimus.meta.metaFkey.MetaFKeyFacadeDTO;
import top.toptimus.repository.meta.tokentemplate.TokenTemplateRepository;
import top.toptimus.tokenTemplate.GeneralViewDTO;
import top.toptimus.tokenTemplate.TokenTemplateDefinitionDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * meta query facade
 *
 * @author lzs
 * @since 2019-8-14
 */
@Component
public class TokenTemplateQueryFacadeEntity {

    /**
     * repository
     */
    @Autowired
    private TokenTemplateRepository tokenTemplateRepository;

    /**
     * 获取TTID下一览
     *
     * @param tokenTemplateId tokenTemplateId
     * @return fKeyDaos
     */
    public TokenTemplateDefinitionDTO findById(String tokenTemplateId) {
        return tokenTemplateRepository.findById(tokenTemplateId);
    }

}
