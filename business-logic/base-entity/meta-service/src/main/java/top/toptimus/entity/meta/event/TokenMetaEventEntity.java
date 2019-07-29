package top.toptimus.entity.meta.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.meta.TokenMetaInformationDto;
import top.toptimus.repository.meta.TokenMetaInformationRepository;

import java.util.List;

/**
 * token meta查询
 * 貌似说的是token和meta关系，待整理，乱糟糟
 *
 * @author gaoyu
 */
@Component
public class TokenMetaEventEntity {
    @Autowired
    private TokenMetaInformationRepository tokenMetaInformationRepository;

    /**
     * 保存meta信息
     * TODO 事务控制
     *
     * @param tokenMetaInformationDtos meta信息定义，token名和类型
     */
    public void saveAll(List<TokenMetaInformationDto> tokenMetaInformationDtos) {
        tokenMetaInformationRepository.deleteAll(tokenMetaInformationDtos);
        tokenMetaInformationRepository.saveAll(tokenMetaInformationDtos);
    }

    /**
     * 删除meta信息
     *
     * @param tokenMetaInformationDtos meta信息定义，token名和类型
     */
    public void deleteAll(List<TokenMetaInformationDto> tokenMetaInformationDtos) {
        tokenMetaInformationRepository.deleteAll(tokenMetaInformationDtos);
    }

    /**
     * 保存meta信息
     *
     * @param tokenMetaInformationDao meta信息定义，token名和类型
     */
    public void save(TokenMetaInformationDto tokenMetaInformationDao) {
        tokenMetaInformationRepository.save(tokenMetaInformationDao);
    }
}
