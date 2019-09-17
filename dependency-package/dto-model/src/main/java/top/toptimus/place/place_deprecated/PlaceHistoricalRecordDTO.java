package top.toptimus.place.place_deprecated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 包含单据提交时间和提交的placedto信息
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PlaceHistoricalRecordDTO implements Serializable {

    private static final long serialVersionUID = 8006001012990682584L;

    private String id;  // 主键
    private String billTokenId;  //表头tokenid
    private String userTaskId;   //任务节点id
    private String taskName;     //任务名
    private String taskMan;      //负责人
    private String timeOfSubmit; //任务提交时间
    private String fileUrl;      //文件路径
    private String tokenTemplateId;   //ttid

}
