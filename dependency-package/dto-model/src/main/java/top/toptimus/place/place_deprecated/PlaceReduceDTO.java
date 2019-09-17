package top.toptimus.place.place_deprecated;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@NoArgsConstructor
@Setter
@Getter
public class PlaceReduceDTO implements Serializable {

    private String billTokenId; // 表头Token信息
    private String billMetaId;  // 表头Meta信息
    private Map<String, List<String>> entryIds = new HashMap<>(); // 分录 K：metaId V:tokenId

    /**
     * 根据PlaceDTO 构建
     *
     * @param placeDTO 库所
     * @return this
     */
    public PlaceReduceDTO build(PlaceDTO placeDTO) {

        this.billTokenId = placeDTO.getBillTokenId();
        this.billMetaId = placeDTO.getBillMetaId();

        placeDTO.getDatas().keySet().forEach(metaId -> {
            if (!placeDTO.getBillMetaId().equals(metaId))
                this.entryIds.put(metaId, new ArrayList<String>() {{
                    placeDTO.getDatas().get(metaId).forEach(tokenDataDto -> add(tokenDataDto.getTokenId()));
                }});
        });
        return this;
    }
}
