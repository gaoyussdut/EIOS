package top.toptimus.place.placeSaveResult;

import lombok.Data;
import lombok.NoArgsConstructor;
import top.toptimus.place.place_deprecated.PlaceDTO;

import java.io.Serializable;

/**
 * 库所保存结果
 *
 * @author gaoyu
 */
@Data
@NoArgsConstructor
public class PlaceSaveResultDTO implements Serializable {
    private static final long serialVersionUID = 2896303151591567668L;
    private String msg;
    private int resultCode;
    private PlaceSaveResultBody placeSaveResultBody;
    private boolean isUpdateIndex = false;  //  是否更新索引

    /**
     * 返回错误信息
     *
     * @param msg 错误信息
     * @return this
     */
    public PlaceSaveResultDTO buildErrorMessage(String msg) {
        this.resultCode = -1;
        this.msg = msg;
        return this;
    }

    /**
     * 正常返回数据体
     *
     * @param placeDTO 库所
     * @return this
     */
    public PlaceSaveResultDTO buildSuccessBody(PlaceDTO placeDTO) {
        this.resultCode = 0;
        this.placeSaveResultBody = new PlaceSaveResultBody(placeDTO);
        return this;
    }

    /**
     * 标记要更新索引
     */
    public void buildUpdateIndex() {
        this.isUpdateIndex = true;
    }
}
