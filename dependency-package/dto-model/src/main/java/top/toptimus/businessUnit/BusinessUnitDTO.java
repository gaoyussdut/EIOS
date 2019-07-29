package top.toptimus.businessUnit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 业务单元
 *
 * @author lizongsheng
 * @since 2019-04-14
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BusinessUnitDTO {

    private String businessUnitCode;  //业务单元id
    private String businessUnitName;  //业务单元名称
    private String billMetaId;   //表头meta  项目式业务单元有，非项目式没有
    private String bpmnURL;      //bpmn图（配置用）
    private String exceptionId;  //异常单ID
    private String description;   //业务单元描述
    private String orgId;         //组织架构id

}
