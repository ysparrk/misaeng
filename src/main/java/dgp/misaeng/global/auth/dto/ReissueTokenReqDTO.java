package dgp.misaeng.global.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReissueTokenReqDTO {
    private String accessToken;

    public ReissueTokenReqDTO() {
        super();
    }

    @Builder
    public ReissueTokenReqDTO(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return "ReissueTokenReqDTO{" +
                "accessToken='" + accessToken + '\'' +
                '}';
    }
}
