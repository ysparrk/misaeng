package dgp.misaeng.domain.device.controller;

import dgp.misaeng.domain.device.dto.reponse.DeviceResDTO;
import dgp.misaeng.domain.device.dto.request.DeviceReqDTO;
import dgp.misaeng.domain.device.service.DeviceService;
import dgp.misaeng.global.dto.ResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/devices")
@RestController
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping
    public ResponseEntity<ResponseDTO> save(
            @PathVariable Long memberId,
            @Valid @RequestBody DeviceReqDTO deviceReqDTO
            ) {

        deviceService.saveDevice(memberId, deviceReqDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("기기 등록 성공")
                        .build());
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> getDevices(
            @PathVariable Long memberId
    ) {
        List<DeviceResDTO> devices = deviceService.getDevices(memberId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("기기 정보 조회 성공")
                        .data(devices)
                        .build());

    }
}
