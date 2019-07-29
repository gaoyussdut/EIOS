package top.toptimus.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * Created by JiangHao on 2019/6/12.
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TopFieldVerifyException extends RuntimeException {

    private static final long serialVersionUID = 2464258760178786813L;

    // 错误码
    private TopErrorCode errorCode;
    // 错误细节
    private Map<String, List<FieldVerifyFailDTO>> errorDetails;  // K:tokenId  V:错误的细节


}
