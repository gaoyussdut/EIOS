package top.toptimus.meta;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 记录x_pk_x_talend_id 下的所有meta的json （用于对比是否有更新）
 * Created by JiangHao on 2018/8/7.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TalendModelMetaDto implements Serializable {

    private static final long serialVersionUID = 237549440637409401L;

    private Integer Id;
    private String talendId;
    private String metaId;
    private String metaJson;  // meta已json格式存储
    private Integer status; // 是否有变动  (0：无变动 1：有变动)

    public TalendModelMetaDto buildStatus(Integer status) {
        this.status = status;
        return this;
    }


}
