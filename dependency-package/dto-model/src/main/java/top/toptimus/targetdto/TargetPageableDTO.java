package top.toptimus.targetdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.toptimus.filter.FilterDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * 目标数据表分页标准DTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TargetPageableDTO {

    private Integer pageSize;
    private Integer pageNo;
    private Integer total;
    private List<TargetDataDTO> targetDataDTOList = new ArrayList<>();

    public TargetPageableDTO(Integer pageSize, Integer pageNo) {
        this.pageSize = pageSize;
        this.pageNo = pageNo;
    }

    public TargetPageableDTO build(Integer total) {
        this.total = total;
        return this;
    }

    public TargetPageableDTO build(List<TargetDataDTO> targetDataDTOList) {
        this.targetDataDTOList = targetDataDTOList;
        return this;
    }

    public String buildQuerySql(List<FilterDTO> filterCondition) {
        String sql = " WHERE ";
        for (FilterDTO filterDTO : filterCondition) {
            switch (filterDTO.getConditionEnum()) {
                case EQUAL: {
                    switch (filterDTO.getFkeyType()) {
                        case STRING:
                            sql += filterDTO.getKey() + " = '" + filterDTO.getFilterCondition() + "' AND ";
                        case DATE:
                            sql += filterDTO.getKey() + " = '" + filterDTO.getFilterCondition() + "' AND ";
                        case DECIMAL:
                            sql += filterDTO.getKey() + " = " + filterDTO.getFilterCondition() + " AND ";
                        case INTEGER:
                            sql += filterDTO.getKey() + " = " + filterDTO.getFilterCondition() + " AND ";
                    }
                }
                case BETWEEN: {
                    String a[] = filterDTO.getFilterCondition().split(",");
                    switch (filterDTO.getFkeyType()) {
                        case DATE:
                            sql += filterDTO.getKey() + " BETWEEN '" + a[0] + "' AND '" + a[1] + " AND ";
                        case DECIMAL:
                            sql += filterDTO.getKey() + " BETWEEN '" + a[0] + "' AND '" + a[1] + " AND ";
                        case INTEGER:
                            sql += filterDTO.getKey() + " BETWEEN '" + a[0] + "' AND '" + a[1] + " AND ";
                    }
                }
                case LIKE: {
                    switch (filterDTO.getFkeyType()) {
                        case STRING:
                            sql += filterDTO.getKey() + " LIKE '%" + filterDTO.getFilterCondition() + "%' AND ";
                    }
                }
            }
        }
        return sql;
    }
}
