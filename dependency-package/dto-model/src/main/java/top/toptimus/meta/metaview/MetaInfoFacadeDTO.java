package top.toptimus.meta.metaview;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 针对top.toptimus.businessmodel.process.dto.meta.dto.metaview.MetaInfoDTO的二次封装
 * controller中使用
 *
 * @author gaoyu
 * @since 2018-07-05
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MetaInfoFacadeDTO implements Serializable {

    private static final long serialVersionUID = 552067094093126993L;

    private String metaId;
    private String metaName;
    private String metaType;
    private int selfId;
    private int parentId;
    private List<MetaInfoDTO> metaEditDTOS;

    /**
     * 构造函数 metaInfo
     *
     * @param metaId       meta id
     * @param metaType     meta type
     * @param metaEditDTOS meta字段
     */
    public MetaInfoFacadeDTO(String metaId, String metaType, List<MetaInfoDTO> metaEditDTOS) {
        this.metaId = metaId;
        this.metaType = metaType;
        this.metaEditDTOS = metaEditDTOS;
    }
}
