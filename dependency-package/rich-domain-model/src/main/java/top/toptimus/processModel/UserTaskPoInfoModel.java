package top.toptimus.processModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.toptimus.place.place_deprecated.PlaceDTO;
import top.toptimus.processDefinition.UserTaskInsDTO;

import java.util.List;

/**
 * 实例化流程节点的任务model
 */
@AllArgsConstructor
@Getter
public class UserTaskPoInfoModel {
    private List<UserTaskInsDTO> userTaskInsDTOS;
    private String userTaskId;
    private String billTokenId;
    private PlaceDTO placeDTO;
}
