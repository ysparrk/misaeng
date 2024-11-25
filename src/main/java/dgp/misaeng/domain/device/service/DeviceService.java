package dgp.misaeng.domain.device.service;

import dgp.misaeng.domain.device.dto.reponse.DeviceResDTO;
import dgp.misaeng.domain.device.dto.request.DeviceReqDTO;

import java.util.List;

public interface DeviceService {
    void saveDevice(Long memberId, DeviceReqDTO deviceReqDTO);
    List<DeviceResDTO> getDevices(Long memberId);
}
