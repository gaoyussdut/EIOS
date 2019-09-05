package top.toptimus.targetdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * 目标类型分页标准DTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TargetTypePageableDTO {

    private Integer pageSize;
    private Integer pageNo;
    private Integer total;
    private List<TargetTypeDTO> targetTypeDTOList = new ArrayList<>();

    public TargetTypePageableDTO(Integer pageSize, Integer pageNo) {
        this.pageSize = pageSize;
        this.pageNo = pageNo;
    }

    public TargetTypePageableDTO build(Integer total) {
        this.total = total;
        return this;
    }

    public TargetTypePageableDTO build(List<TargetTypeDTO> targetTypeDTOList) {
        this.targetTypeDTOList = targetTypeDTOList;
        return this;
    }
}
