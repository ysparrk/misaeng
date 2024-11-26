package dgp.misaeng.domain.device.controller;

import dgp.misaeng.domain.device.dto.reponse.DeviceResDTO;
import dgp.misaeng.domain.device.dto.request.*;
import dgp.misaeng.domain.device.service.DeviceService;
import dgp.misaeng.global.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/devices")
@RestController
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping("/{memberId}")
    public ResponseEntity<ResponseDTO> save(
            @PathVariable Long memberId,
            @RequestBody DeviceReqDTO deviceReqDTO
            ) {

        deviceService.saveDevice(memberId, deviceReqDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("기기 등록 성공")
                        .build());
    }

    @GetMapping("/{memberId}")
    public ResponseEntity<ResponseDTO> getDevices(
            @PathVariable Long memberId
    ) {
        List<DeviceResDTO> devices = deviceService.getDevices(memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("기기 및 미생물 목록 조회 성공")
                        .data(devices)
                        .build());

    }

    @PutMapping("/empty")
    public ResponseEntity<ResponseDTO> emptyOn(
            @RequestBody DeviceEmptyStateReqDTO deviceEmptyStateReqDTO
            ) {
        deviceService.setEmptyState(deviceEmptyStateReqDTO.getSerialNum(), deviceEmptyStateReqDTO.isEmptyState());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("기기 자리비움 상태 전달 성공")
                        .build());
    }


    @PutMapping("/mode")
    public ResponseEntity<ResponseDTO> updateMode(
            @RequestBody DeviceModeReqDTO deviceModeReqDTO
    ) {
        deviceService.updateDeviceMode(deviceModeReqDTO.getDeviceId(), deviceModeReqDTO.getDeviceMode());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("기기 제어 모드 설정 성공")
                        .build());
    }

    @PostMapping("/capsule-cycle")
    public ResponseEntity<ResponseDTO> updateCapsuleCycle(
            @RequestBody DeviceCapsuleCycleReqDTO deviceCapsuleCycleReqDTO
    ) {
        deviceService.updateCapsuleCycle(deviceCapsuleCycleReqDTO.getDeviceId(), deviceCapsuleCycleReqDTO.getCapsuleCycle());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("캡슐 시간 주기 설정 성공")
                        .build());
    }

    @PostMapping("/empty-active-time")
    public ResponseEntity<ResponseDTO> updateEmptyActiveTime(
            @RequestBody DeviceEmptyActiveTimeReqDTO deviceEmptyActiveTimeReqDTO
            ) {
        deviceService.updateEmptyActiveTime(deviceEmptyActiveTimeReqDTO.getDeviceId(), deviceEmptyActiveTimeReqDTO.getEmptyActiveTime());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("자리비움 활성화 대기 시간 설정 성공")
                        .build());
    }



}
