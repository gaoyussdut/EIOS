package top.toptimus.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.toptimus.entity.DataModelEntity;
import top.toptimus.meta.TokenMetaInfoDTO;
import top.toptimus.tokendata.TokenDataDto;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by JiangHao on 2018/8/2.
 */
@Service
public class DataModelService {

    @Autowired
    private DataModelEntity dataModelEntity;


    /**
     * 从talend model中生成数据视图meta
     *
     * @param x_pk_x_talend_id talend model id
     * @return K:表名 token meta id，V:数据视图meta
     */
    public Map<String, TokenMetaInfoDTO> getTokenMetaInfoDTOS(String x_pk_x_talend_id) throws IOException {
        return dataModelEntity.getTokenMetaInfoDTOS(x_pk_x_talend_id);
    }


    /**
     * 分页查询data_model_pOJO，返回：K:talend主键，V:data model名
     *
     * @param pageNo   页码
     * @param pageSize 页宽
     * @return K:talend主键，V:data model名
     */
    public Map<String, String> getTalendDataModelMeta(int pageNo, int pageSize) {
        return dataModelEntity.getTalendDataModelMeta(pageNo, pageSize);
    }


    /**
     * 根据表名返回数据
     *
     * @param tokenMetaInfoDTO meta信息
     * @param pageNo           页码
     * @param pageSize         长度
     * @return token数据列表
     */
    public List<TokenDataDto> getTalendData(
            TokenMetaInfoDTO tokenMetaInfoDTO
            , int pageNo
            , int pageSize
    ) {
        return dataModelEntity.getTalendData(tokenMetaInfoDTO, pageNo, pageSize);
    }

    //  ------------------------
    //  下面这一堆没有被调用过
    //  ------------------------

    /**
     * 根据schema id和tokenMetaId取主数据meta
     *
     * @param x_pk_x_talend_id talend model id
     * @param tokenMetaId      token meta id
     * @return TokenMetaInfoDTO
     */
    public TokenMetaInfoDTO getTokenMetaInfoDTO(String x_pk_x_talend_id, String tokenMetaId) throws IOException {
        return dataModelEntity.getTokenMetaInfoDTO(x_pk_x_talend_id, tokenMetaId);
    }
}
