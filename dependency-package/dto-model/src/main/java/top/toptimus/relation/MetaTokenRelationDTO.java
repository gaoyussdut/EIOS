package top.toptimus.relation;

import lombok.Data;
import lombok.NoArgsConstructor;
import top.toptimus.place.PlaceDTO;

import java.io.Serializable;

/**
 * 后置关系
 * 记录表头token和（分录、关联单据、引用单据）之间的token关系
 *
 * @author gaoyu
 * @since 2019-01-24
 */
@Data
@NoArgsConstructor
public class MetaTokenRelationDTO implements Serializable {
    private static final long serialVersionUID = -9088374938340108566L;
    protected String id;  //  ES id
    private String targetBillMetaId;    //  关联的(目标)单据、分录、引用的单据的metaid
    private String targetBillTokenId;//关联的单据、分录、引用的单据的tokenId

    public MetaTokenRelationDTO(String id, String sourceBillMetaId, String sourceBillTokenId) {
        //  源单关系
        this.id = id;
        this.targetBillMetaId = sourceBillMetaId;
        this.targetBillTokenId = sourceBillTokenId;
    }

    /**
     * 创建place之间的关系
     *
     * @param placeDTO       当前place
     */
    public MetaTokenRelationDTO(PlaceDTO placeDTO) {
        this.id = placeDTO.getPreMetaTokenRelationDTO().getId();
        this.targetBillMetaId = placeDTO.getBillMetaId();
        this.targetBillTokenId = placeDTO.getBillTokenId();
    }
}
