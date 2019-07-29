package top.toptimus.stock;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.toptimus.common.enums.StockTypeEnum;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 库存明细帐
 *
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StockDetailDTO {

    private String id;              // id
    private String materielId;      // 物料Id
    private BigDecimal amount;      // 数量
    private StockTypeEnum type;     // 出入库类型
    private String tokenId;         // tokenId
    private Timestamp createAt;     // 创建时间
    private String createUser;      // 创建用户 显示的是用户表中的用户名，存储的时候存UserId
    private Timestamp updateAt;     // 更新时间
    private String updateUser;      // 更新用户 显示的是用户表中的用户名，存储的时候存UserId
    private String storage;         // 库位

    private String biaodibianma;
    private String biaodimingcheng;
    private String jiliangdanwei;
    private String shuilv;

    /**
     * 构造方法
     */
    public StockDetailDTO(String id, String materielId,
                          BigDecimal amount, StockTypeEnum type, String tokenId, Timestamp createAt,
                          String createUser,Timestamp updateAt,String updateUser,String storage) {
        this.id = id;
        this.materielId = materielId;
        this.amount = amount;
        this.type = type;
        this.tokenId = tokenId;
        this.createAt = createAt;
        this.createUser = createUser;
        this.updateAt = updateAt;
        this.updateUser = updateUser;
        this.storage = storage;
    }
}
