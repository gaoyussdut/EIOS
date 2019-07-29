package top.toptimus.place.placeRequire;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 备查账信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemorandumInfoBody implements Serializable {

    private static final long serialVersionUID = 7312168327647635443L;

    private String metaId;
    private List<String> tokenIds;
}
