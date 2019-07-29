package top.toptimus.entity.tokendata.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.meta.property.MetaFieldDTO;
import top.toptimus.repository.token.dynamicTokenQuery.DynamicTokenQueryRepository;
import top.toptimus.tokendata.TokenDataDto;

import java.util.ArrayList;
import java.util.List;

/**
 * token事务实体
 *
 * @author gaoyu
 * @since 2018-11-26
 */
@Component
public class TokenDataSqlCUDEntity {

    @Autowired
    private DynamicTokenQueryRepository dynamicTokenQueryRepository;

    /**
     * 插入token数据
     *
     * @param tableName        表名
     * @param tokenDataDtoList 数据
     * @param metaFields       meta
     */
    public void insertTokenData(String tableName, List<TokenDataDto> tokenDataDtoList, List<MetaFieldDTO> metaFields) {
        // insert处理
        if (tokenDataDtoList != null && tokenDataDtoList.size() > 0) {
            this.dynamicTokenQueryRepository.generateSave(tokenDataDtoList, tableName, metaFields);
        }
    }

    /**
     * 更新token数据
     *
     * @param tableName        表名
     * @param tokenDataDtoList 数据
     * @param metaFields       meta
     */
    public void updateTokenData(String tableName, List<TokenDataDto> tokenDataDtoList, List<MetaFieldDTO> metaFields) {
        // update处理
        if (tokenDataDtoList != null && tokenDataDtoList.size() > 0) {
            List<TokenDataDto> tokenDataDtos = new ArrayList<TokenDataDto>() {{
                tokenDataDtoList.forEach(tokenDataDto -> add(new TokenDataDto(tokenDataDto, metaFields)));
            }};

            for (TokenDataDto tokenDataDto : tokenDataDtos) {
                dynamicTokenQueryRepository.generateUpdate(tokenDataDto, tableName, metaFields);
            }
        }
    }

}
