package dgp.misaeng.domain.environment.dto.request;

import lombok.Getter;

@Getter
public class EnvironmentReqDTO {
    private String sericalNum;
    private float temperature;
    private float humidity;

    public EnvironmentReqDTO() {}

}
