package top.toptimus.formula;

import lombok.Getter;
import top.toptimus.formula.formula.Parser;
import top.toptimus.formula.util.DefaultProperties;
import top.toptimus.place.place_deprecated.PlaceDTO;
import top.toptimus.tokendata.TokenDataDto;
import top.toptimus.tokendata.field.FkeyField;

import java.util.ArrayList;
import java.util.List;

/**
 * UserTask公式model
 *
 * @author gaoyu
 */
@Getter
public class UserTaskFormulaModel {
    List<String> toUserTaskIds = new ArrayList<>(); //可跳转的节点列表
    String defaultToUserTaskId;                     //默认跳转节点

    public UserTaskFormulaModel(List<UserTaskFormulaDTO> userTaskFormulaDTOS, PlaceDTO placeDTO, String billMetaId) {
        userTaskFormulaDTOS.forEach(userTaskFormulaDTO -> {
            String formula = userTaskFormulaDTO.getFormula();
            //1,为公式赋值
            for (TokenDataDto tokenDataDto : placeDTO.getDatas().get(billMetaId)) {
                for (FkeyField fkeyField : tokenDataDto.getFields()) {
                    formula = formula.replace("@" + fkeyField.getKey() + "@", fkeyField.getJsonData());

                }
            }
            //2,判断toUserTaskId是否为默认
            if (userTaskFormulaDTO.isIsdefault()) {
                defaultToUserTaskId = userTaskFormulaDTO.getToUsertaskId();
            }
            //3,根据公式的计算结果判断是否可跳转到该toUserTask
            if (Boolean.parseBoolean(
                    new Parser()
                            .parse(formula)
                            .getValue(new DefaultProperties())
                            .getValue().toString())) {
                toUserTaskIds.add(userTaskFormulaDTO.getToUsertaskId());
            }
        });

        //4,若根据公式计算全部节点都不可跳转,则跳转到默认节点
        if (toUserTaskIds.size() == 0 && null != defaultToUserTaskId) {
            toUserTaskIds.add(defaultToUserTaskId);
        }
    }
}
