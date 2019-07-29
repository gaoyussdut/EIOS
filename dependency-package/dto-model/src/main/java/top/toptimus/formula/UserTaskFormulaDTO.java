package top.toptimus.formula;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserTaskFormulaDTO implements Serializable {
    private static final long serialVersionUID = -5510069430731589313L;

    private String processId;      //流程ID
    private String fromUsertaskId; //from节点
    private String toUsertaskId;   //to节点
    private String formula;       //公式
    private boolean isdefault;     //是否默认

    public UserTaskFormulaDTO build(String processId, String fromUsertaskId, String toUsertaskId, String formula, boolean isdefault) {
        this.processId = processId;
        this.fromUsertaskId = fromUsertaskId;
        this.toUsertaskId = toUsertaskId;
        this.formula = formula;
        this.isdefault = isdefault;
        return this;
    }
}
