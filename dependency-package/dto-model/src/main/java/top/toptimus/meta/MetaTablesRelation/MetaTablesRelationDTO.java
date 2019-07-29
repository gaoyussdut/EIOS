package top.toptimus.meta.MetaTablesRelation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import top.toptimus.meta.metaFkey.ProcessTableDTO;

/**
 * meta id和数据库表对应的关系
 *
 * @author gaoyu
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MetaTablesRelationDTO {

    private int id;
    private String processId;
    private String tableName;
    private String vertexId;
    private String metaId;

    public MetaTablesRelationDTO build(ProcessTableDTO processTableDTO) {
        this.tableName = processTableDTO.getTableName();
        this.metaId = processTableDTO.getMetaId();
        return this;
    }
}
