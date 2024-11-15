package dgp.misaeng.global.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ReissueTokenResDTO {
    private String accessToken;

    public ReissueTokenResDTO() {
        super();
    }

    @Builder
    public ReissueTokenResDTO(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return "ReissueTokenReqDTO{" +
                "accessToken='" + accessToken + '\'' +
                '}';
    }
}
