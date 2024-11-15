package dgp.misaeng.global.dto;

import dgp.misaeng.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ErrorResponseDTO {

    private String code;
    private String message;

    public static ErrorResponseDTO of (ErrorCode code) {
        return new ErrorResponseDTO(code.getCode(), code.getMessage());
    }
}