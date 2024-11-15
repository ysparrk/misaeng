package dgp.misaeng.global.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ResponseDTO {
    private String message;
    private Object data;

    @Builder
    public ResponseDTO(String message, Object data) {
        this.message = message;
        this.data = data;
    }

    @Builder
    public ResponseDTO(String message) {
        this.message = message;
    }
}
