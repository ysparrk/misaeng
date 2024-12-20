package dgp.misaeng.domain.device.service;

import dgp.misaeng.domain.device.dto.reponse.DeviceRegisterResDTO;
import dgp.misaeng.domain.device.dto.reponse.DeviceResDTO;
import dgp.misaeng.domain.device.dto.reponse.DeviceStateResDTO;
import dgp.misaeng.domain.device.dto.request.DeviceReqDTO;
import dgp.misaeng.global.util.enums.DeviceMode;

import java.util.List;

public interface DeviceService {
    DeviceRegisterResDTO saveDevice(Long memberId, DeviceReqDTO deviceReqDTO);
    List<DeviceResDTO> getDevices(Long memberId);

    //제어
    void setEmptyState(String serialNum, boolean emptyState);
    void updateDeviceMode(Long deviceId, DeviceMode deviceMode);
    void updateCapsuleCycle(Long deviceId, Integer time);
    void updateEmptyActiveTime(Long deviceId, Integer time);
    void updateCloseWaitTime(Long deviceId, float second);
    DeviceStateResDTO getDeviceState(String serialNum);
}
