package top.toptimus.dao.token;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import top.toptimus.place.place_deprecated.PlaceDTO;
import top.toptimus.relation.MetaTokenRelationDTO;
import top.toptimus.relation.PreMetaTokenRelationDTO;

/**
 * 记录表头token和（分录、关联单据、引用单据）之间的token关系
 */
@NoArgsConstructor
@Data
@Document(indexName = "meta_token_rel", type = "meta_token_rel", shards = 1, replicas = 0, refreshInterval = "-1")
public class MetaTokenRelationDao {
    @Id
    private String Id;
    private String billMetaId; //单据主数据meta
    private String billTokenId;//单据表头tokenId
    private String entryMetaId;//关联的单据、分录、引用的单据的metaid
    private String entryTokenId;//关联的单据、分录、引用的单据的tokenId

    public MetaTokenRelationDTO build() {
        return new MetaTokenRelationDTO(
                this.Id
                , this.entryMetaId
                , this.entryTokenId
        );
    }

    public PreMetaTokenRelationDTO buildPreMetaTokenRel() {
        return new PreMetaTokenRelationDTO(
                this.Id
                , this.billMetaId
                , this.billTokenId
        );
    }


    /**
     * 构造函数
     */
    public MetaTokenRelationDao(String id, String billMetaId, String billTokenId, String entryMetaId, String entryTokenId) {
        this.Id = id;
        this.billMetaId = billMetaId;
        this.billTokenId = billTokenId;
        this.entryMetaId = entryMetaId;
        this.entryTokenId = entryTokenId;
    }

    /**
     * 根据库所生成表头和分录的关系
     *
     * @param placeDTO 库所
     */
    public MetaTokenRelationDao(PlaceDTO placeDTO) {
        this.Id = placeDTO.getPreMetaTokenRelationDTO().getId();
        this.billMetaId = placeDTO.getPreMetaTokenRelationDTO().getSourceBillMetaId();
        this.billTokenId = placeDTO.getPreMetaTokenRelationDTO().getSourceBillTokenId();
        this.entryMetaId = placeDTO.getBillMetaId();
        this.entryTokenId = placeDTO.getBillTokenId();
    }

    /**
     * 根据库所和库所保存结果生成表头和分录的关系
     *
     * @param placeDTO               库所
     * @param billTokenSaveResultDTO 库所保存结果
     */
//    public MetaTokenRelationDao(PlaceDTO placeDTO, BillTokenSaveResultDTO billTokenSaveResultDTO) {
//        this.Id = UUID.randomUUID().toString();
//        this.billMetaId = placeDTO.getBillMetaId();
//        this.billTokenId = placeDTO.getBillTokenId();
//        this.entryMetaId = billTokenSaveResultDTO.getBillTokenResultBody().getMetaId();
//        this.entryTokenId = billTokenSaveResultDTO.getBillTokenResultBody().getTokenId();
//    }
}
