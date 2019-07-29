package top.toptimus.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.meta.TalendMetaInfo;
import top.toptimus.meta.TokenMetaInfoDTO;
import top.toptimus.repository.DataModelRepository;
import top.toptimus.repository.TalendDataRepository;
import top.toptimus.tokendata.TokenDataDto;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DataModelEntity {

    @Autowired
    private DataModelRepository dataModelRepository;
    @Autowired
    private TalendDataRepository talendDataRepository;
    @Autowired
    private DataModelEntity dataModelEntity;


    /**
     * 从talend model中生成数据视图meta
     *
     * @param x_pk_x_talend_id talend model id
     * @return K:表名 token meta id，V:数据视图meta
     */
    public Map<String, TokenMetaInfoDTO> getTokenMetaInfoDTOS(String x_pk_x_talend_id) throws IOException {
        return dataModelRepository.getSchema(x_pk_x_talend_id);
    }

    /**
     * 根据schema id和tokenMetaId取主数据meta
     *
     * @param x_pk_x_talend_id talend model id
     * @param tokenMetaId      token meta id
     * @return TokenMetaInfoDTO
     */
    public TokenMetaInfoDTO getTokenMetaInfoDTO(String x_pk_x_talend_id, String tokenMetaId) throws IOException {
        return dataModelRepository.getTokenMetaInfoDTO(x_pk_x_talend_id, tokenMetaId);
    }

    /**
     * 根据schema id和tokenMetaId取主数据meta
     *
     * @param x_pk_x_talend_id talend model id
     * @param tokenMetaId      token meta id
     * @param key              字段
     * @return TalendMetaInfo
     */
    public TalendMetaInfo getTalendMetaInfo(String x_pk_x_talend_id, String tokenMetaId, String key) throws IOException {
        return dataModelRepository.getTalendMetaInfo(x_pk_x_talend_id, tokenMetaId, key);
    }

    /**
     * 分页查询data_model_pOJO，返回：K:talend主键，V:data model名
     *
     * @param pageNo   页码
     * @param pageSize 页宽
     * @return K:talend主键，V:data model名
     */
    public Map<String, String> getTalendDataModelMeta(int pageNo, int pageSize) {
        return new HashMap<String, String>() {{
            //  分页查询所有xsd
            dataModelRepository.getAllSchema(pageNo, pageSize)
                    .forEach(result -> put(
                            result.get("x_pk_x_talend_id").toString()   //  data_model_pOJO主键
                            , result.get("x_name").toString()   //  data_model_pOJO名
                    ));
        }};
    }

    /**
     * 根据表名返回数据
     *
     * @param x_pk_x_talend_id talend model id
     * @param tokenMetaId      token meta id
     * @param pageNo           页码
     * @param pageSize         长度
     * @return token数据列表
     */
    public List<TokenDataDto> getTalendData(
            String x_pk_x_talend_id
            , String tokenMetaId
            , int pageNo
            , int pageSize
    ) throws IOException {
        return talendDataRepository.getData(
                dataModelEntity.getTokenMetaInfoDTO(x_pk_x_talend_id, tokenMetaId)
                , pageNo
                , pageSize
        );
    }

    /**
     * 根据表名返回数据
     *
     * @param tokenMetaInfoDTO token meta
     * @param pageNo           页码
     * @param pageSize         长度
     * @return token数据列表
     */
    public List<TokenDataDto> getTalendData(
            TokenMetaInfoDTO tokenMetaInfoDTO
            , int pageNo
            , int pageSize
    ) {
        return talendDataRepository.getData(
                tokenMetaInfoDTO
                , pageNo
                , pageSize
        );
    }

    /**
     * 取schema中所有关联定义
     *
     * @param x_pk_x_talend_id        talend model id
     * @param referenceEntityTypeName 联表名
     * @return 关联定义
     */
    public List<TalendMetaInfo> getTalendReference(String x_pk_x_talend_id, String referenceEntityTypeName) throws IOException {
        return dataModelRepository.getTalendMetaInfo(x_pk_x_talend_id, referenceEntityTypeName);
    }


    /**
     * 取schema中所有关联定义
     *
     * @param x_pk_x_talend_id        talend model id
     * @param referenceEntityTypeName 主表名
     * @param fieldEntityTypeName     关联表名
     * @return 关联表的外键
     */
    public String getTalendFieldPath(String x_pk_x_talend_id, String referenceEntityTypeName, String fieldEntityTypeName) throws IOException {
        List<TalendMetaInfo> talendMetaInfos = dataModelRepository.getTalendMetaInfo(x_pk_x_talend_id, referenceEntityTypeName);
        for (TalendMetaInfo talendMetaInfo : talendMetaInfos) {
            if (fieldEntityTypeName.equals(talendMetaInfo.getFieldEntityTypeName())) {
                return talendMetaInfo.getFieldPath();
            }
        }
        return null;
    }
}
