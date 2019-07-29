package top.toptimus.repository;

import com.google.common.base.Joiner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import top.toptimus.common.enums.FkeyTypeEnum;
import top.toptimus.meta.TokenMetaInfoDTO;
import top.toptimus.meta.property.MetaFieldDTO;
import top.toptimus.tokendata.TokenDataDto;
import top.toptimus.tokendata.field.FkeyField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * talend数据表中数据的repo
 *
 * @author gaoyu
 */
@Repository
public class TalendDataRepository {
    @Autowired
    @Qualifier("talendJdbcTemplate")
    private JdbcTemplate talendJdbcTemplate;

    /**
     * 根据表名返回数据
     *
     * @param tokenMetaInfoDTO meta信息
     * @param pageNo           页码
     * @param pageSize         长度
     * @return token数据列表
     */
    public List<TokenDataDto> getData(
            TokenMetaInfoDTO tokenMetaInfoDTO
            , int pageNo
            , int pageSize
    ) {
        String args = Joiner.on(",").join(
                new ArrayList<String>() {{
                    tokenMetaInfoDTO.getMetaFields().forEach(metaField -> {
                        //  talend外键清洗
                        if (FkeyTypeEnum.SELECT.name().equals(metaField.getType().getType().name())) {
                            //  外键
                            add("x_" + metaField.getKey() + "_x_id as " + metaField.getKey());
                        } else {
                            add("x_" + metaField.getKey() + " as " + metaField.getKey());
                        }
                    });
                }}
        );

        return new ArrayList<TokenDataDto>() {{
            talendJdbcTemplate.queryForList(
                    "select " + args + " from "
                            + tokenMetaInfoDTO.getTokenMetaId() //  表名
                            + " limit " + pageNo + "," + pageSize + ";"
            ).forEach(resultSet -> {    //  遍历结果集，生成token data列表
                add(
                        TalendDataRepository.getTokenDataDto(resultSet, tokenMetaInfoDTO)   //  生成token data
                );
            });
        }};

    }

    /**
     * 生成token data
     *
     * @param resultSet        结果集
     * @param tokenMetaInfoDTO meta
     * @return token data
     */
    private static TokenDataDto getTokenDataDto(Map<String, Object> resultSet, TokenMetaInfoDTO tokenMetaInfoDTO) {
        try {
            //  token data 的key数据列表
            List<FkeyField> fkeyFields = new ArrayList<FkeyField>() {{
                for (String key : resultSet.keySet()) {
                    if (!"id".equals(key)) {
                        if (resultSet.containsKey(key) && null != resultSet.get(key)) { //  当数据存在的时候才抽取FkeyField
                            FkeyField fkeyField = TalendDataRepository.generateFkeyField(key, resultSet, tokenMetaInfoDTO);
                            if (null != fkeyField)
                                add(fkeyField);
                        }
                    }
                }
            }};
            return new TokenDataDto(resultSet.get("id").toString(), fkeyFields);    //  生成token data
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("数据转换异常");
        }
    }

    /**
     * 生成token data的每一个key的数据
     *
     * @param key              key，也就是字段
     * @param resultSet        结果集
     * @param tokenMetaInfoDTO meta
     * @return token data一个key的数据
     */
    private static FkeyField generateFkeyField(String key, Map<String, Object> resultSet, TokenMetaInfoDTO tokenMetaInfoDTO) {
        for (MetaFieldDTO metaField : tokenMetaInfoDTO.getMetaFields()) {
            if (metaField.getKey().equals(key)) {
                if (metaField.getType().getType().name()
                        .equals(FkeyTypeEnum.SELECT.name())) {
                    return new FkeyField().createSelectFkeyField(metaField.getType().getType(), key, resultSet.get(key).toString(), "");
                } else {
                    return new FkeyField().createPlainFkeyField(metaField.getType().getType(), key, resultSet.get(key).toString());
                }
            }
        }
        return null;
    }
}
