package top.toptimus.relation;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 前置关系
 * 记录表头token和（分录、关联单据、引用单据）之间的token关系
 *
 * @author gaoyu
 * @since 2019-01-24
 */
@Data
@NoArgsConstructor
public class PreMetaTokenRelationDTO implements Serializable {
    private static final long serialVersionUID = -9088374938340108566L;
    protected String id;  //  ES id
    protected String sourceBillMetaId; // 源单主数据meta
    protected String sourceBillTokenId; //  源单表头tokenId

    public PreMetaTokenRelationDTO(String id, String sourceBillMetaId, String sourceBillTokenId) {
        this.id = id;
        this.sourceBillMetaId = sourceBillMetaId;
        this.sourceBillTokenId = sourceBillTokenId;
    }

    public PreMetaTokenRelationDTO(String sourceBillMetaId, String sourceBillTokenId) {
        this.sourceBillMetaId = sourceBillMetaId;
        this.sourceBillTokenId = sourceBillTokenId;
    }

}
