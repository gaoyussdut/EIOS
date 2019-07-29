package top.toptimus.dao.meta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import top.toptimus.common.search.SearchItem;

import java.util.List;

/**
 * meta检索配置dao
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(indexName = "meta_search", type = "meta_search_config", shards = 1, replicas = 0, refreshInterval = "-1")
public class MetaSearchConfigDao {
    @Id
    private String metaId; // meta id
    @Field(type = FieldType.Nested)
    private List<SearchItem> metaSearchDaos; // 查询条件

}
