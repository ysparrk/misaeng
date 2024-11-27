package dgp.misaeng.domain.device.dto.reponse;

import dgp.misaeng.domain.microbe.dto.reponse.MicrobeInfoResDTO;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DeviceResDTO {
    private Long deviceId;
    private String deviceName;
    private String serialNum;
    MicrobeInfoResDTO microbeInfo;

    @Builder
    public DeviceResDTO(Long deviceId, String deviceName, String serialNum, MicrobeInfoResDTO microbeInfo) {
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.serialNum = serialNum;
        this.microbeInfo = microbeInfo;
    }
}
