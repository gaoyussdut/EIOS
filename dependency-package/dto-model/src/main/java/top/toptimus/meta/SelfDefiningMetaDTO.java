package top.toptimus.meta;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户自定义meta
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class SelfDefiningMetaDTO implements Serializable {

    private static final long serialVersionUID = 237549440637409401L;

    private String memorandvnMetaId; //备查帐meta
    private String userId; //用户ID
    private String selfDefiningMeta; //自定义metaId
    private String defaultMetaId; // 默认metaId

    public SelfDefiningMetaDTO(String memorandvnMetaId, String userId, String selfDefiningMeta) {
        this.memorandvnMetaId = memorandvnMetaId;
        this.userId = userId;
        this.selfDefiningMeta = selfDefiningMeta;
    }
}
