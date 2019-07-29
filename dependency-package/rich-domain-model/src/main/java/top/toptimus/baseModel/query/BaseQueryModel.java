package top.toptimus.baseModel.query;

import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.baseModel.BaseModel;
import top.toptimus.dynamicSQLModel.queryFacadeModel.ViewQueryMainTable;
import top.toptimus.tokendata.ReducedTokenDataDto;
import top.toptimus.tokendata.TokenDataDto;
import top.toptimus.tokendata.TokenDataPageableDto;
import top.toptimus.tokendata.field.FkeyField;
import top.toptimus.tokendata.field.ReducedFkeyField;

import java.util.ArrayList;
import java.util.List;

/**
 * 查询基类
 */
@Getter
@NoArgsConstructor
public abstract class BaseQueryModel extends BaseModel {
    protected ViewQueryMainTable mainTable;  //  主表结构
    protected List<TokenDataDto> tokenDataDtos;
    protected List<ReducedTokenDataDto> reducedTokenDataDtoList = new ArrayList<>();  // 清洗后要返回的reducedTokenDataDtoList
    protected TokenDataPageableDto tokenDataPageableDto;

    /**
     * 生成查询的sql
     */
    protected BaseQueryModel buildStrSQL(List<String> tokenIds) {
        this.mainTable.buildViewQuery(tokenIds);
        return this;
    }

    protected void buildStrSQL(String strSQL) {
        this.mainTable.buildStrSQL(strSQL);
    }

    public BaseQueryModel buildViewQuery() {
        this.buildStrSQL(this.mainTable.getStrSql());
        return this;
    }

    public BaseQueryModel buildTokenDatas(List<TokenDataDto> tokenDataDtos) {
        this.tokenDataDtos = tokenDataDtos;
        return this;
    }

    /**
     * 将TokenDataDTO数据简化为只包涵key,businessId,jsonData的ReducedTokenDataDto
     *
     * @param cleanTokenDataDtoList 待清洗token data列表
     * @return reducedTokenDataDtoList
     */
    public BaseQueryModel buildCleanTokenDataDto(List<TokenDataDto> cleanTokenDataDtoList) {
        // 将TokenDataDto中的key,businessId,jsonData存入简化DTO中
        for (TokenDataDto tokenDataDto : cleanTokenDataDtoList) {
            List<ReducedFkeyField> reducedFkeyField = new ArrayList<>();
            // 遍历FkeyField中的数据
            for (FkeyField fkeyField : tokenDataDto.getFields()) {
                reducedFkeyField.add(
                        new ReducedFkeyField(fkeyField.getKey(), fkeyField.getBusinessId(), fkeyField.getJsonData()));
            }
            this.reducedTokenDataDtoList.add(new ReducedTokenDataDto(tokenDataDto.getTokenId(), reducedFkeyField));
        }
        if (null == tokenDataPageableDto) {
            this.tokenDataPageableDto = new TokenDataPageableDto().build(this.reducedTokenDataDtoList);// 如果为null的场合，赋值
        } else {
            this.tokenDataPageableDto.build(this.reducedTokenDataDtoList); // 不为null的场合，需要构建数据，防止Page相关信息丢失
        }

        return this;
    }
}
