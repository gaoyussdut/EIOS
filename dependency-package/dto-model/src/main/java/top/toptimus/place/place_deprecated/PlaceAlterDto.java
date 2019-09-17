package top.toptimus.place.place_deprecated;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import top.toptimus.common.enums.DomainTypeEnum;
import top.toptimus.exception.TopErrorCode;
import top.toptimus.exception.TopException;

import java.io.Serializable;
import java.util.List;

/**
 * 库所增量提交dto
 *
 * @author gaoyu
 * @since 2018-12-03
 */
@NoArgsConstructor
@Setter
@Getter
public class PlaceAlterDto implements Serializable {
    private String billTokenId;
    private DomainTypeEnum domainTypeEnum;
    private String billMetaId;  //  表头meta id
    private String metaId;  //  当前meta id
    private List<String> alters;    //  变更的token id（新增和值变更）
    private List<String> removes;   //  删除的token id

    /**
     * 判断表头分录，构建库所增量提交dto
     *
     * @param billTokenId    表头token id
     * @param metaId         meta id
     * @param metaType       meta类型
     * @param billMetaId     表头token id
     * @param removeTokenIds 删除的token id
     */
    public PlaceAlterDto(String billTokenId, String metaId, String metaType, String billMetaId, List<String> removeTokenIds) {
        this.billTokenId = billTokenId;
        this.metaId = metaId;
        this.removes = removeTokenIds;
        if (metaType.equals(DomainTypeEnum.BILL.name())) {
            this.domainTypeEnum = DomainTypeEnum.BILL;
            this.billMetaId = metaId;
        } else if (metaType.equals(DomainTypeEnum.ENTRY.name())) {
            this.domainTypeEnum = DomainTypeEnum.ENTRY;
            this.billMetaId = billMetaId;
        } else
            throw new TopException(TopErrorCode.META_TYPE_ERR); //meta类型有问题
    }

    public void buildAlters(List<String> alters) {
        this.alters = alters;
    }

}
