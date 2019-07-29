package top.toptimus.businessunit;

import lombok.Data;
import lombok.NoArgsConstructor;
import top.toptimus.businessUnit.BusinessUnitDTO;
import top.toptimus.businessUnit.BusinessUnitEdgeDTO;
import top.toptimus.common.enums.TaskStatusEnum;
import top.toptimus.tokendata.TokenDataDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 非项目式业务单元实体
 *
 * @author lzs
 * @since 2019-04-27
 */
@Data
@NoArgsConstructor
public class BusinessUnitModel {

    private BusinessUnitDTO businessUnitDTO; //业务单元DTO

    private String metaId; //当前节点metaId

    private String TaskInsDTO; // 当前节点索引
    private TaskStatusEnum status; //当前节点状态

    private TokenDataDto billTokenData; //表头数据
    private Map<String, List<TokenDataDto>> entryDatas; //分录数据

    private List<String> nextNewMetaIds = new ArrayList<>(); //下级可多条新建meta
    private List<String> nextNewOnlyMetaIds = new ArrayList<>(); //下级可唯一新建meta
    private List<String> nextPushDownAutoMetaIds = new ArrayList<>(); //下级可自动下推meta
    private List<String> nextPushDownSSMetaIds = new ArrayList<>(); //下级可手动下推SSmeta
    private List<String> nextPushDownFSMetaIds = new ArrayList<>(); //下级可手动下推FSmeta
    private List<String> nextReverseMetaIds = new ArrayList<>(); //可反写meta

    private String statusRuleStoredProcedure; //状态规则存储过程

    /**
     * 构建业务单元下级meta
     * @param businessUnitEdgeDTOS
     * @return
     */
    public BusinessUnitModel build(List<BusinessUnitEdgeDTO> businessUnitEdgeDTOS) {
        businessUnitEdgeDTOS.forEach(businessUnitEdgeDTO -> {
            switch (businessUnitEdgeDTO.getEdgeType()) {
                case NEW: {
                    this.nextNewMetaIds.add(businessUnitEdgeDTO.getToMetaId());
                    break;
                }
                case NEW_ONLY: {
                    this.nextNewOnlyMetaIds.add(businessUnitEdgeDTO.getToMetaId());
                    break;
                }
                case PUSHDOWN_AUTO: {
                    this.nextPushDownAutoMetaIds.add(businessUnitEdgeDTO.getToMetaId());
                    break;
                }
                case PUSHDOWN_SS: {
                    this.nextPushDownSSMetaIds.add(businessUnitEdgeDTO.getToMetaId());
                    break;
                }
                case PUSHDOWN_FS: {
                    this.nextPushDownFSMetaIds.add(businessUnitEdgeDTO.getToMetaId());
                    break;
                }
                case REVERSE: {
                    this.nextReverseMetaIds.add(businessUnitEdgeDTO.getToMetaId());
                    break;
                }
            }
        });
        return this;

    }
}
