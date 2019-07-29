package top.toptimus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.toptimus.entity.DataModelEntity;
import top.toptimus.meta.TalendMetaInfo;

import java.io.IOException;
import java.util.List;

@Service
public class DataModelRuntimeService {
    @Autowired
    private DataModelEntity dataModelEntity;

    /**
     * 运行时调用，用来获取关联键，用来做关联查询，之前的SELECT类型逻辑
     *
     * @param x_pk_x_talend_id talend model id
     * @param tokenMetaId      token meta id
     * @param key              字段
     * @return TalendMetaInfo
     */
    public TalendMetaInfo getTalendMetaInfo(String x_pk_x_talend_id, String tokenMetaId, String key) throws IOException {
        return dataModelEntity.getTalendMetaInfo(x_pk_x_talend_id, tokenMetaId, key);
    }

    /**
     * 取schema中所有关联定义
     *
     * @param x_pk_x_talend_id        talend model id
     * @param referenceEntityTypeName 联表名
     * @return 关联定义
     */
    public List<TalendMetaInfo> getTalendReference(String x_pk_x_talend_id, String referenceEntityTypeName) throws IOException {
        return dataModelEntity.getTalendReference(x_pk_x_talend_id, referenceEntityTypeName);
    }
}
