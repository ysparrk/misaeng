package dgp.misaeng.domain.device.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DeviceReqDTO {
    private String serialNum;
    private String deviceType;
    private String deviceName;
    private String microbeName;

    public DeviceReqDTO() {}

    @Builder
    public DeviceReqDTO(String serialNum, String deviceType, String deviceName, String microbeName) {
        this.serialNum = serialNum;
        this.deviceType = deviceType;
        this.deviceName = deviceName;
        this.microbeName = microbeName;
    }
}
