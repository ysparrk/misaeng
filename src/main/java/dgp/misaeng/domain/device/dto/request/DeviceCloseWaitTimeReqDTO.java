package dgp.misaeng.domain.device.dto.request;

import lombok.Getter;

@Getter
public class DeviceCloseWaitTimeReqDTO {
    private Long deviceId;
    private float closeWaitTime;
}
