package top.toptimus.model.procedure;

import lombok.Getter;
import top.toptimus.baseModel.query.BaseQueryModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 调用存储过程model
 *
 * @author gaoyu
 * @since 2019-01-07
 */
@Getter
public class ProcedureModel extends BaseQueryModel {
    private String billTokenId;
    private String lotNo;
    private List<String> storedProcedures = new ArrayList<>();

    /**
     * 判断存储过程是否为空，为空报错
     *
     * @param billTokenId      bill token id
     * @param lotNo            lot no
     * @param storedProcedures 存储过程列表
     */
    public ProcedureModel(
            String billTokenId
            , String lotNo
            , List<String> storedProcedures
    ) {
        this.billTokenId = billTokenId;
        this.lotNo = lotNo;
        try {
            if (null == storedProcedures || storedProcedures.isEmpty())
                this.buildErrorMessage(false, "存储过程为空");
            else
                this.storedProcedures = storedProcedures;
        } catch (Exception e) {
            this.buildErrorMessage(false, "存储过程为空");
        }
    }

}
