package top.toptimus.meta.metaFkey;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * t_token_meta_fkey对应的DTO
 *
 * @author gaoyu
 * @since 2018-07-04
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MetaFKeyDTO {
    private String fKey;
    private String caption;  //显示的字段
    private String fkeyType; // 数据类型（包含select）
}
