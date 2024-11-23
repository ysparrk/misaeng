package dgp.misaeng.domain.capsule.controller;

import dgp.misaeng.domain.capsule.dto.request.CapsuleReqDTO;
import dgp.misaeng.domain.capsule.service.CapsuleService;
import dgp.misaeng.global.dto.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
