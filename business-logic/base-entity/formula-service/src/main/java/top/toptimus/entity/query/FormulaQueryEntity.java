package top.toptimus.entity.query;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.toptimus.common.CurrentPage;
import top.toptimus.formula.FormulaDTO;
import top.toptimus.formula.FormulaModel;
import top.toptimus.formula.PredefinedFormationDTO;
import top.toptimus.repository.FormulaRepository;
import top.toptimus.repository.PredefinedFormationRepository;

import java.util.List;

/**
 * Created by JiangHao on 2018/9/7.
 */
@Component
public class FormulaQueryEntity {

    @Autowired
    private FormulaRepository formulaRepository;
    @Autowired
    private PredefinedFormationRepository predefinedFormationRepository;

    /**
     * 查看全部公式
     * 已经配置好的公式
     *
     * @param pageNo   当前页
     * @param pageSize 每页的大小
     * @return 全部公式 分页
     */
    public CurrentPage<FormulaDTO> getAllFormula(Integer pageNo, Integer pageSize) {
        return formulaRepository.findAll(pageNo, pageSize);
    }

    /**
     * 获取全部预定义好的公式
     * 用于配置页面 可以选择预定义的公式进行配置
     *
     * @return 全部预定义公式
     */
    public List<PredefinedFormationDTO> getAllPredefinedFormation() {
        return predefinedFormationRepository.findAll();
    }

    /**
     * 根据业务纬度id 取公式
     *
     * @param businessAspectId 业务纬度id
     * @return 公式
     */
    public List<FormulaDTO> getFormulaByBusinessAspectId(String businessAspectId) {
        return new FormulaModel(
                formulaRepository.findByBusinessAspectId(businessAspectId)
        ).getFormulaDTOS();
    }
}