package dgp.misaeng.domain.microbe.dto.request;

import lombok.Getter;

@Getter
public class MicrobeEnvironmentReqDTO {
    private String serialNum;
    private float temperature;
    private float humidity;
    private float gas;

    public MicrobeEnvironmentReqDTO() {}
}
