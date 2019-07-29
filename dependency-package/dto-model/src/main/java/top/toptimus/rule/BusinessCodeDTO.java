package top.toptimus.rule;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 事务码DTO
 * Created by lzs
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BusinessCodeDTO implements Serializable {

    private static final long serialVersionUID = -9171156207628591454L;

    private String businessCode;    //事务码
    private String ruleName;
    private String originMetaId;    //来源单据meta
    private String targetMetaId;    //目标单据meta
    private String status;          //状态
    private String type;
}
