package dgp.misaeng.domain.microbe.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MicrobeNewReqDTO {
    private Long deviceId;
    private String microbeName;

    @Builder
    public MicrobeNewReqDTO(Long deviceId, String microbeName) {
        this.deviceId = deviceId;
        this.microbeName = microbeName;
    }
}
