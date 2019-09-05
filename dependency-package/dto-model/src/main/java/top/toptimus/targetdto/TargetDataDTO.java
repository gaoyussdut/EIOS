package top.toptimus.targetdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 目标管理标准DTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TargetDataDTO {

    private String targetDataId;   //目标数据表编码
    private String targetDataTableName;     //目标数据表名
    private String targetTypeId;    //目标类型Id
    private boolean isSplit;     //是否拆分
    private Integer chaijiejici;   //拆分后的纵轴
    private String riqikeli;    //横轴
    private String status;      //状态
    private Date createTime;  //创建时间
    private String targetUnitId;  //目标计量单位
    private String targetBill;  //目标对应单据
    private String targetBillKey; //目标对应单据字段
    private String taskCycle;   //任务周期
    private String year;    //年份

}
