package top.toptimus.targetdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 目标类型标准DTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TargetTypeDTO {

    private String targetTypeId;
    private String targetType;
    private String remark;
    private String ouId;

}
