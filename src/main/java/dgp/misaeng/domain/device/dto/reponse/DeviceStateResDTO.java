package dgp.misaeng.domain.device.dto.reponse;

import dgp.misaeng.global.util.enums.DeviceMode;
import lombok.Builder;
import lombok.Getter;

@Getter
public class DeviceStateResDTO {
    private DeviceMode deviceMode;
    private boolean emptyState;
    private Integer emptyActiveTime;
    private Integer capsuleCycle;
    private float closeWaitTime;

    @Builder
    public DeviceStateResDTO(DeviceMode deviceMode, boolean emptyState, Integer emptyActiveTime, Integer capsuleCycle, float closeWaitTime) {
        this.deviceMode = deviceMode;
        this.emptyState = emptyState;
        this.emptyActiveTime = emptyActiveTime;
        this.capsuleCycle = capsuleCycle;
        this.closeWaitTime = closeWaitTime;
    }
}
