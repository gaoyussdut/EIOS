package top.toptimus.entity.eventHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.formula.FormulaDTO;
import top.toptimus.formula.FormulaDefinitionDTO;
import top.toptimus.repository.FormulaRepository;
import top.toptimus.repository.FumulaDefinitionRepository;
import top.toptimus.repository.UserTaskFormulaRepository;

import java.util.List;

/**
 * Created by JiangHao on 2018/9/7.
 */
@Component
public class FormulaSaveEntity {

    @Autowired
    private FormulaRepository formulaRepository;
    @Autowired
    private UserTaskFormulaRepository userTaskFormulaRepository;
    @Autowired
    private FumulaDefinitionRepository fumulaDefinitionRepository;

    /**
     * 保存全部公式
     *
     * @param formulaDTOS 公式
     */
    public void saveAllFormula(List<FormulaDTO> formulaDTOS) {
        formulaRepository.saveAll(formulaDTOS);
    }

    /**
     * 根据业务纬度id  删除关联的所有公式
     *
     * @param businessAspectId 业务纬度id
     */
    public void delectByBusinessAspectId(String businessAspectId) {
        formulaRepository.delectByBusinessAspectId(businessAspectId);
    }

    /**
     * 根据主键删除公式
     *
     * @param id 主键
     */
    public void delectFormulaById(Integer id) {
        formulaRepository.delectById(id);
    }

    /**
     * 根据processId  删除关联的所有公式
     *
     * @param processId 流程ID
     */
    public void delectByProcessId(String processId) {
        userTaskFormulaRepository.delectByProcessId(processId);

    }

    /**
     * 保存全部公式
     *
     * @param formulaDefinitionDTOs 公式
     */
    public void saveAllFormulaDefinition(List<FormulaDefinitionDTO> formulaDefinitionDTOs) {
        fumulaDefinitionRepository.saveAll(formulaDefinitionDTOs);
    }


}
