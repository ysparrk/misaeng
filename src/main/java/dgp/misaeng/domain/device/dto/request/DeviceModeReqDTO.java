package dgp.misaeng.domain.device.dto.request;

import dgp.misaeng.global.util.enums.DeviceMode;
import lombok.Getter;

@Getter
public class DeviceModeReqDTO {
    private Long deviceId;
    private DeviceMode deviceMode;
}
