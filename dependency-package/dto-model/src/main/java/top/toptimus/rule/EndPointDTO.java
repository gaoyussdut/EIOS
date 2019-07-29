package top.toptimus.rule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * EndPointDTO
 * Created by lzs
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EndPointDTO {
    private String ruleId;
    private String url;
    private String inputParam;
}
