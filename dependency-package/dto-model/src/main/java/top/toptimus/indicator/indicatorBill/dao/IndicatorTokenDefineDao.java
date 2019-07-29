package top.toptimus.indicator.indicatorBill.dao;

import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.indicator.indicatorBill.dto.IndicatorOuRelDto;

/**
 * 指标单据转换日志
 * 指标单据最后通过归集凭证，来实现数据归集
 * 凭证是冷数据归集，ods层面，基于ER建模
 * 后续可以做基于数据仓库做热数据归集——比ods中的数据集更大，基于维度建模
 *
 * @author gaoyu
 * @since 2019-7-16
 */
@Getter
@NoArgsConstructor
public class IndicatorTokenDefineDao {
    private String tokenId; //  业务主键，表头id
    private String metaId;  //  meta id

    private String sourceTokenId;   //  源单表头token id
    private String sourceMetaId;    //  源单meta id

    private String targetOuId;    //  新建或者下推生成指标token的时候，归属的业务组织id——用户前台手选
    private String sourceOuId;  //  源单ou id

    /**
     * 记录业务转换关系，无论下推或者新建，都要调用
     *
     * @param indicatorOuRelDto 指标单据meta和ou的关系配置
     * @param sourceTokenId     源单表头token id
     * @param targetTokenId     目标表头token id
     */
    public IndicatorTokenDefineDao(
            IndicatorOuRelDto indicatorOuRelDto
            , String sourceTokenId
            , String targetTokenId
    ) {
        this.tokenId = targetTokenId; //  新单据token id
        this.metaId = indicatorOuRelDto.getTargetMetaId();  //  目标单据meta id
        this.sourceTokenId = sourceTokenId; //  源单id
        this.sourceMetaId = indicatorOuRelDto.getSourceMetaId();  //  源单meta id
        this.targetOuId = indicatorOuRelDto.getTargetOuId();    //  目标ou id
        this.sourceOuId = indicatorOuRelDto.getSourceOuId();    //  源ou id
    }
}
