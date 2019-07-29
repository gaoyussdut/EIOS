package top.toptimus.model.tokenModel.event;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import top.toptimus.baseModel.BaseModel;
import top.toptimus.businessmodel.memorandvn.dto.ForeignKeyDTO;
import top.toptimus.token.TokenDto;
import top.toptimus.tokendata.TokenDataDto;

import java.util.*;

/**
 * 分录保存的充血模型
 */
@Getter
public class TokenEntrySaveModel extends BaseModel {
    private Map<String, String> metaIdAndTableName;
    private Map<String, ForeignKeyDTO> tableNameAndForeignKeyDTO;
    private Map<String, List<TokenDataDto>> metaIdAndTokensMap;
    private String billTokenId;
    private List<TokenDto> tokenDtos = new ArrayList<>();
    private Map<String, List<TokenDto>> tableNameAndTokenDtos = new HashMap<>();
    private List<TokenDto> insertTokenDtos = new ArrayList<>();
    private List<TokenDto> updateTokenDtos = new ArrayList<>();

    private String billMetaId;

    public TokenEntrySaveModel(String billMetaId, Map<String, List<TokenDataDto>> metaIdAndTokensMap, String billTokenId) {
        this.billMetaId = billMetaId;
        this.metaIdAndTokensMap = metaIdAndTokensMap;
        this.billTokenId = billTokenId;
    }

    /**
     * 分录的表和外键关系
     *
     * @param metaIdAndTableName tableName即是主数据分录的mataId  //  K:meta id,V:表名
     * @param foreignKeyDTOList  根据metaId 查询外键列表
     * @return this
     */
    public TokenEntrySaveModel build(Map<String, String> metaIdAndTableName, List<ForeignKeyDTO> foreignKeyDTOList) {
        this.tableNameAndForeignKeyDTO = this.getForeignKeyByMetaId(
                foreignKeyDTOList,
                new ArrayList<>(metaIdAndTableName.values())
        );
        this.metaIdAndTableName = metaIdAndTableName;

        for (String entryMetaId : metaIdAndTableName.keySet()) {
            String tableName = metaIdAndTableName.get(entryMetaId);// tableName即是主数据分录的mataId
            // 2、根据两个metaId找到分录对应的表里面的的外键字段
            ForeignKeyDTO foreignKeyDTO = tableNameAndForeignKeyDTO.get(tableName);

            for (TokenDataDto dto : metaIdAndTokensMap.get(entryMetaId)) {
                // 设置tokenId
                if (StringUtils.isEmpty(dto.getTokenId())) {
                    dto.build(UUID.randomUUID().toString());
                }

                TokenDto tokenDto = new TokenDto().build(dto.getTokenId(), tableName, foreignKeyDTO.getFieldPath(), billTokenId);
                this.tokenDtos.add(tokenDto);
                if (this.tableNameAndTokenDtos.keySet().contains(tableName)) {
                    this.tableNameAndTokenDtos.get(tableName).add(tokenDto);
                } else {
                    this.tableNameAndTokenDtos.put(tableName, Lists.newArrayList(tokenDto));
                }
            }
        }
        return this;
    }

    /**
     * 根据metaId和fieldEntityTypeNames取得ForeignKeyDTO
     *
     * @param foreignKeyDTOList    外键列表
     * @param fieldEntityTypeNames 主表，对应referenceEntityTypeName
     * @return 外键
     */
    private Map<String, ForeignKeyDTO> getForeignKeyByMetaId(List<ForeignKeyDTO> foreignKeyDTOList, List<String> fieldEntityTypeNames) {
        return new HashMap<String, ForeignKeyDTO>() {{
            try {
                for (ForeignKeyDTO dto : foreignKeyDTOList) {
                    for (String fieldEntityTypeName : fieldEntityTypeNames)
                        if (fieldEntityTypeName.equals(dto.getFieldEntityTypeName().toLowerCase())) {
                            put(fieldEntityTypeName, dto);
                        }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }};
    }

    /**
     * 根据 TokenDao的 tokenId 在数据库是否存在
     * 将 TokenDao 分为两类 insert&update
     *
     * @param exitsTokenIds 已存在的token id列表
     * @return PlaceEntrySaveModel
     */
    public TokenEntrySaveModel build(List<String> exitsTokenIds) {
        this.tokenDtos.forEach(tokenDao -> {
            {
                if (exitsTokenIds.contains(tokenDao.getTokenId())) {
                    this.updateTokenDtos.add(tokenDao);
                } else {
                    this.insertTokenDtos.add(tokenDao);
                }
            }
        });
        return this;
    }
}
