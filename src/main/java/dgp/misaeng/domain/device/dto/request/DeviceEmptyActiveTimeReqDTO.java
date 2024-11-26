package dgp.misaeng.domain.device.dto.request;

import lombok.Getter;

@Getter
public class DeviceEmptyActiveTimeReqDTO {
    private Long deviceId;
    private Integer emptyActiveTime;
}
