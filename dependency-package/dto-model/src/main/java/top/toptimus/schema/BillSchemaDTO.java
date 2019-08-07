package top.toptimus.schema;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import top.toptimus.common.enums.MetaTypeEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 业务描述：返回前段的单据描述画面DTO
 * @author lzs
 * @since 2019-08-06
 */
@NoArgsConstructor
@Setter
@Getter
public class BillSchemaDTO implements Serializable {

    private int order_;  //展示顺序
    private String metaId;
    private String tokenId;
    private List<String> tokenIdList = new ArrayList<>();
    private MetaTypeEnum metaType;

    public BillSchemaDTO (String metaId,MetaTypeEnum metaType,int order_){
        this.metaId = metaId;
        this.metaType = metaType;
        this.order_ = order_;
    }

    public BillSchemaDTO build(String metaId,String tokenId,MetaTypeEnum metaType){
        this.metaId=metaId;
        this.tokenId = tokenId;
        this.metaType=metaType;
        return this;
    }

    public BillSchemaDTO build(String tokenId){
        this.tokenIdList.add(tokenId);
        return this;
    }
}
