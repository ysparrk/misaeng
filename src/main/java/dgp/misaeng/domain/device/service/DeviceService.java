package dgp.misaeng.domain.device.service;

import dgp.misaeng.domain.device.dto.request.DeviceReqDTO;

public interface DeviceService {
    void saveDevice(Long memberId, DeviceReqDTO deviceReqDTO);

}
