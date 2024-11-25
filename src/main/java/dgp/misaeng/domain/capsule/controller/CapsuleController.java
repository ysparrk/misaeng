package dgp.misaeng.domain.capsule.controller;

import dgp.misaeng.domain.capsule.dto.request.CapsuleListReqDTO;
import dgp.misaeng.domain.capsule.dto.request.CapsuleReqDTO;
import dgp.misaeng.domain.capsule.dto.response.CapsuleResDTO;
import dgp.misaeng.domain.capsule.service.CapsuleService;
import dgp.misaeng.global.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/capsules")
@RestController
@RequiredArgsConstructor
public class CapsuleController {

    private final CapsuleService capsuleService;

    @PostMapping
    public ResponseEntity<ResponseDTO> saveCapsuleUse(
            @RequestBody CapsuleReqDTO capsuleReqDTO) {

        capsuleService.useCapsule(capsuleReqDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("캡슐 사용 기록 저장 성공")
                        .build());
    }

    @GetMapping
    public ResponseEntity<ResponseDTO> getCapsule(
            @RequestBody CapsuleListReqDTO capsuleListReqDTO
    ) {

        CapsuleResDTO capsuleInfoList = capsuleService.getCapsule(capsuleListReqDTO.getMicrobeId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("잔여 캡슐량 조회 성공")
                        .data(capsuleInfoList)
                        .build());
    }

}
