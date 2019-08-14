package top.toptimus.tokenTemplate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.tokendata.TokenDataDto;
import top.toptimus.tokendata.TokenDataPageableDto;

import java.util.List;

/**
 * 单据一览返回标准DTO
 *
 * @author lzs
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class GeneralViewDTO {

    private String tokenTemplateId;
    private String billMetaId;
    private TokenDataPageableDto datas;

    public GeneralViewDTO(String tokenTemplateId,String billMetaId){
        this.tokenTemplateId = tokenTemplateId;
        this.billMetaId = billMetaId;
    }

    public GeneralViewDTO build(TokenDataPageableDto datas){
        this.datas = datas;
        return this;
    }

}
