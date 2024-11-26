package dgp.misaeng.domain.device.dto.request;

import lombok.Getter;

@Getter
public class DeviceEmptyStateReqDTO {
    private String serialNum;
    private boolean emptyState;
}
