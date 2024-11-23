package dgp.misaeng.domain.microbe.dto.reponse;

import dgp.misaeng.global.util.enums.EnvironmentState;
import lombok.Builder;

public class MicrobeEnvironmentResDTO {
    private float temperature;
    private float humidity;
    private EnvironmentState temperatureState;
    private EnvironmentState humidityState;

    @Builder
    public MicrobeEnvironmentResDTO(float temperature, float humidity, EnvironmentState temperatureState, EnvironmentState humidityState) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.temperatureState = temperatureState;
        this.humidityState = humidityState;
    }
}
