package top.toptimus.schema;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import top.toptimus.common.enums.MetaTypeEnum;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务描述：作为返给前段预览页面DTO。包含表头信息及相关联分录及单据信息
 *
 * @author lzs
 * @since 2019-08-06
 */
@NoArgsConstructor
@Setter
@Getter
public class BillPreviewDTO {

    private BillSchemaDTO billHeader = new BillSchemaDTO(); //表头
    private List<BillSchemaDTO> relBillDTOList = new ArrayList<>(); //关联单据列表

    public BillPreviewDTO build(String tokenTemplateId, String billMetaId, String billTokenId, MetaTypeEnum metaType) {
        this.billHeader.build(tokenTemplateId, billMetaId, billTokenId, metaType);
        return this;
    }

    public BillPreviewDTO build(String tokenId) {
        this.billHeader.setTokenId(tokenId);
        return this;
    }
}
