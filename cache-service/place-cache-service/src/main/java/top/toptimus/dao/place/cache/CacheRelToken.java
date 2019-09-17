package top.toptimus.dao.place.cache;

import lombok.Data;
import lombok.NoArgsConstructor;
import top.toptimus.place.place_deprecated.PlaceDTO;
import top.toptimus.relation.MetaTokenRelationDTO;
import top.toptimus.relation.PreMetaTokenRelationDTO;

@SuppressWarnings("ALL")
@NoArgsConstructor
@Data
class CacheRelToken {
    private String relId;   //  关联主键
    private String tokenId;
    private String metaId;

    /**
     * 构造函数
     *
     * @param metaTokenRelationDTO 记录表头token和（分录、关联单据、引用单据）之间的token关系
     */
    public CacheRelToken(MetaTokenRelationDTO metaTokenRelationDTO) {
        this.relId = metaTokenRelationDTO.getId();
        this.tokenId = metaTokenRelationDTO.getTargetBillTokenId(); //  后置关系
        this.metaId = metaTokenRelationDTO.getTargetBillMetaId();   //  后置关系
    }

    /**
     * 构造函数
     *
     * @param relId   关联主键
     * @param tokenId 前置单据tokenid
     * @param metaId  前置单据metaId
     */
    public CacheRelToken(String relId, String tokenId, String metaId) {
        this.relId = relId;
        this.tokenId = tokenId;
        this.metaId = metaId;
    }

    /**
     * 构造函数
     *
     * @param preMetaTokenRelationDTO 前置关系
     */
    public CacheRelToken(PreMetaTokenRelationDTO preMetaTokenRelationDTO) {
        this.relId = preMetaTokenRelationDTO.getId();
        this.tokenId = preMetaTokenRelationDTO.getSourceBillTokenId();
        this.metaId = preMetaTokenRelationDTO.getSourceBillMetaId();
    }
    /**
     * 构造函数
     *
     * @param placeDTO   前置单据
     */
    public CacheRelToken(PlaceDTO placeDTO) {
        this.relId = placeDTO.getPreMetaTokenRelationDTO().getId();
        this.tokenId = placeDTO.getBillTokenId();
        this.metaId = placeDTO.getBillMetaId();
    }
}
