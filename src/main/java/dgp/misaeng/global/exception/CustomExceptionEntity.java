package dgp.misaeng.global.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class CustomExceptionEntity {
    private String errorCode;
    private String errorMessage;

    public CustomExceptionEntity(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }
}