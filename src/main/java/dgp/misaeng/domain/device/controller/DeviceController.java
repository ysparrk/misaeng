package dgp.misaeng.domain.device.controller;

import dgp.misaeng.domain.device.dto.request.DeviceReqDTO;
import dgp.misaeng.domain.device.service.DeviceService;
import dgp.misaeng.global.dto.ResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/devices")
@RestController
@RequiredArgsConstructor
public class DeviceController {

    private final DeviceService deviceService;

    @PostMapping
    public ResponseEntity<ResponseDTO> save(
            @AuthenticationPrincipal Long memberId,
            @Valid @RequestBody DeviceReqDTO deviceReqDTO
            ) {

        deviceService.saveDevice(memberId, deviceReqDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("기기 등록 성공")
                        .build());
    }


}
