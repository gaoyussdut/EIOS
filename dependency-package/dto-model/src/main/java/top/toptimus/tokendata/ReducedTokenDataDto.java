package top.toptimus.tokendata;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.tokendata.field.ReducedFkeyField;

import java.io.Serializable;
import java.util.List;

/**
 * 只包含key,businessId,jsonData的简化TokenDataDTO
 *
 * @author lizongsheng
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ReducedTokenDataDto implements Serializable {

    private static final long serialVersionUID = 7972459176951747484L;

    private String tokenId;
    private List<ReducedFkeyField> fields;

}
