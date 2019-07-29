package top.toptimus.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TopException extends RuntimeException {
    private static final long serialVersionUID = 6625L;
    // 错误码
    private TopErrorCode errorCode;
    // 错误细节
    private String errorDetails;

    public TopException(TopErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public String TopExceptionInfos() {
        String infos = "";
        if (errorCode != null) {
            infos = errorCode.toString();
        } else
            return infos;

        if (errorDetails != null) {
            String formatString = "----------";
            infos += formatString + errorDetails;
        }
        return infos;
    }
}
