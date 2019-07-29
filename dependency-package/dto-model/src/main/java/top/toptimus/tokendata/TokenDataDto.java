package top.toptimus.tokendata;

import lombok.Data;
import top.toptimus.constantConfig.Constants;
import top.toptimus.exception.TopErrorCode;
import top.toptimus.exception.TopException;
import top.toptimus.meta.TokenMetaInfoDTO;
import top.toptimus.meta.property.MetaFieldDTO;
import top.toptimus.tokendata.field.FkeyField;
import top.toptimus.tokendata.field.ReducedFkeyField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * token数据前端结构
 */
@Data
public class TokenDataDto implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 8632293587675498365L;
    private String tokenId;
    /**
     * k:Fkey,v:data
     */
    private List<FkeyField> fields = new ArrayList<>();

    /**
     * <无参构造函数>
     * 创建一个新的token数据
     */
    public TokenDataDto() {
        this.tokenId = UUID.randomUUID().toString();
    }

    public TokenDataDto(String tokenId) {
        this.tokenId = tokenId;
    }

    public TokenDataDto(String tokenId, ArrayList<FkeyField> fkeyFields) {
        if (tokenId.isEmpty())
            throw new TopException(TopErrorCode.TOKEN_ID_NOT_EXIST);// token id 为空！
        this.tokenId = tokenId;
        this.fields = fkeyFields;
    }

    public TokenDataDto(List<FkeyField> fields) {
        this.fields = fields;
    }

    /**
     * 根据meta field定义和token数据，取fkey field
     *
     * @param metaField meta字段基础信息
     * @param tokenData token数据
     * @return fkey
     */
    private static FkeyField getFkeyField(MetaFieldDTO metaField, Map<String, Object> tokenData) {
        String jsonData = TokenDataDto.getData(tokenData, metaField.getKey());
        switch (metaField.getType().getType()) {
            case SELECT:
                // SELECT类型
                String businessId = TokenDataDto.getData(tokenData, metaField.getKey() + Constants._businessId);
                return new FkeyField().createSelectFkeyField(metaField.getType().getType(), metaField.getKey(),
                        businessId, jsonData);
            case SELECT_INTERN:
                // SELECT INTERN类型
                return new FkeyField().createSelectInternFkeyField(metaField.getType().getType(),
                        metaField.getKey(), jsonData);
            case BOOLEAN:
                if (jsonData.equals(Constants.jsonData_1) || jsonData.equals(Constants.jsonData_true)) {
                    jsonData = Constants.jsonData_true;
                } else {
                    jsonData = Constants.jsonData_false;
                }
                return new FkeyField().createPlainFkeyField(metaField.getType().getType(), metaField.getKey(),
                        jsonData);
            default:
                return new FkeyField().createPlainFkeyField(metaField.getType().getType(), metaField.getKey(),
                        jsonData);
        }
    }

    /**
     * 根据key取json data
     *
     * @param tokenData token数据
     * @param key       key
     * @return json data
     */
    private static String getData(Map<String, Object> tokenData, String key) {
        if (tokenData.containsKey(key)) {
            String jsonData = String.valueOf(tokenData.get(key));
            if (!jsonData.equals(Constants.nullValue))
                return jsonData;
        }
        return Constants.jsonData_EmptyString;
    }

    public TokenDataDto(String tokenId, List<FkeyField> fields) {
        if (tokenId.isEmpty())
            throw new TopException(TopErrorCode.TOKEN_ID_NOT_EXIST);// token id 为空！
        this.tokenId = tokenId;
        this.fields = fields;
    }

    /**
     * 根据查询结果集构造TokenDataDto
     *
     * @param tokenMetaInfoDTO 元数据
     * @param tokenData        token数据map，K：key，V:value
     * @param tokenId          token id
     */
    public TokenDataDto(String tokenId, TokenMetaInfoDTO tokenMetaInfoDTO, Map<String, Object> tokenData) {
        this(
                tokenId
                , new ArrayList<FkeyField>() {{
                    tokenMetaInfoDTO.getMetaFields().forEach(metaField -> add(TokenDataDto.getFkeyField(metaField, tokenData)));
                }}
        );
    }

    public TokenDataDto build(String tokenId) {
        if (tokenId.isEmpty())
            throw new TopException(TopErrorCode.TOKEN_ID_NOT_EXIST);// token id 为空！
        this.tokenId = tokenId;
        return this;
    }

    public TokenDataDto build(List<FkeyField> fields) {
        this.fields = fields;
        return this;
    }

    /**
     * 数据保存时使用
     * 匹配key的type类型
     * key为intern不保存
     * key为keySelectType保存jsonData的id
     * 其他的key只保存value
     * 如果有老数据判断是否与新数据相同 并将其更新为新的key
     *
     * @param tokenDataDto token数据前端结构
     * @param metaFields   字段列表
     */
    public TokenDataDto(TokenDataDto tokenDataDto, List<MetaFieldDTO> metaFields) {
        // 最终要被持久化的list
        List<FkeyField> fkeyFieldUDTList = new ArrayList<>();
        // 要更新的key
        List<String> updateKeys = new ArrayList<>();
        this.tokenId = tokenDataDto.getTokenId();

        // 新数据
        tokenDataDto.getFields().forEach(
                fkeyField -> {
                    //  key没匹配上的时候报错
                    //  Key是intern，返回空
                    //  keySelectType，jsonData是id
                    //  其他jsonData是value
                    FkeyField fkeyFieldUDT = new FkeyField().build(fkeyField, metaFields);
                    if (null != fkeyFieldUDT) {
                        // 循环匹配新数据和要更新的数据是否相同  如果相同存入要更新的数组
                        for (FkeyField orgFkeyFieldUDT : this.getFields()) {
                            if (orgFkeyFieldUDT.getKey().equals(fkeyFieldUDT.getKey())) {
                                // fkey和以前相同，标记为要更新
                                updateKeys.add(orgFkeyFieldUDT.getKey());
                                break;
                            }
                        }
                        // 标记完之后将整个新数据添加到最终要被持久化的list
                        fkeyFieldUDTList.add(fkeyFieldUDT);
                    }
                }
        );
        // 旧数据
        this.getFields().forEach(fkeyFieldUDT -> {
            // 要被更新的数据不加入新的list中
            if (!updateKeys.contains(fkeyFieldUDT.getKey())) {
                //  key没匹配上的时候，抛出异常
                //  key是intern，返回空
                FkeyField orgFkeyFieldUDT = fkeyFieldUDT.build(metaFields);
                // key如果是intern则orgFkeyFieldUDT为null 不进行保存
                if (null != orgFkeyFieldUDT) {
                    //  key没匹配上的时候报错
                    //  key是intern，返回空
                    //  keySelectType，jsonData是id
                    //  其他jsonData是value
                    fkeyFieldUDTList.add(fkeyFieldUDT.build(metaFields));
                }
            }
        });
        // 将旧数据list清空
        this.getFields().clear();
        this.fields.addAll(fkeyFieldUDTList);
    }

    /**
     * 分页方法
     *
     * @param tokenDataDtoList token数据
     * @param pageSize         pageSize
     * @param pageNo           pageNo
     * @return json data
     */
    public TokenDataPageableDto getTokenDataPageableDto(List<TokenDataDto> tokenDataDtoList, int pageSize, int pageNo) {
        int fromIndex = pageSize * (pageNo - 1);
        int toIndex = pageSize * pageNo - 1 > tokenDataDtoList.size() ? tokenDataDtoList.size() : pageSize * pageNo - 1;
        List<TokenDataDto> cleanTokenDataDto = tokenDataDtoList.subList(fromIndex, toIndex);

        // 将TokenDataDto中的key,businessId,jsonData存入简化DTO中
        List<ReducedTokenDataDto> reducedTokenDataDtoList = new ArrayList<>();
        for (TokenDataDto tokenDataDto : cleanTokenDataDto) {
            List<ReducedFkeyField> reducedFkeyField = new ArrayList<>();
            // 遍历FkeyField中的数据
            for (FkeyField fkeyField : tokenDataDto.getFields()) {
                reducedFkeyField.add(
                        new ReducedFkeyField(fkeyField.getKey(), fkeyField.getBusinessId(), fkeyField.getJsonData()));
            }
            reducedTokenDataDtoList.add(new ReducedTokenDataDto(tokenDataDto.getTokenId(), reducedFkeyField));
        }
        return new TokenDataPageableDto(pageSize, pageNo, reducedTokenDataDtoList);
    }

}