package dgp.misaeng.domain.microbe.dto.request;

import lombok.Getter;

@Getter
public class MicrobeEnvironmentReqDTO {
    private String sericalNum;
    private float temperature;
    private float humidity;

    public MicrobeEnvironmentReqDTO() {}
}
