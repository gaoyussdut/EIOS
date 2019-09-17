package top.toptimus.place.place_deprecated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.toptimus.place.place_deprecated.PlaceDTO;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PlacePDFDTO implements Serializable {

    private static final long serialVersionUID = -6674267944859878443L;

    private String id;
    private String billTokenId;    //表头tokenid
    private String timeOfSubmit;   //任务提交时间
    private PlaceDTO placeDTO;     //历史PlaceDTO
}
