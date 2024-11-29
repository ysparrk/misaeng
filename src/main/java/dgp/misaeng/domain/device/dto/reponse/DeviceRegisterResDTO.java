package dgp.misaeng.domain.device.dto.reponse;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DeviceRegisterResDTO {
    private Long deviceId;
    private String serialNum;
    private Long microbeId;

    @Builder
    public DeviceRegisterResDTO(Long deviceId, String serialNum, Long microbeId) {
        this.deviceId = deviceId;
        this.serialNum = serialNum;
        this.microbeId = microbeId;
    }
}
