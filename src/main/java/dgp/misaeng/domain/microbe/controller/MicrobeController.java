package dgp.misaeng.domain.microbe.controller;

import dgp.misaeng.domain.microbe.dto.reponse.MicrobeEnvironmentResDTO;
import dgp.misaeng.domain.microbe.dto.reponse.MicrobeInfoResDTO;
import dgp.misaeng.domain.microbe.dto.request.MicrobeEnvironmentReqDTO;
import dgp.misaeng.domain.microbe.dto.request.MicrobeRecordReqDTO;
import dgp.misaeng.domain.microbe.dto.request.MicrobeReqDTO;
import dgp.misaeng.domain.microbe.service.MicrobeService;
import dgp.misaeng.global.dto.ResponseDTO;
import dgp.misaeng.global.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/microbes")
@RestController
@RequiredArgsConstructor
public class MicrobeController {

    private final MicrobeService microbeService;
    private final RedisService redisService;

    @PostMapping("/evironments")
    public ResponseEntity<ResponseDTO> saveEnvironment(
            @RequestBody MicrobeEnvironmentReqDTO microbeEnvironmentReqDTO) {

        redisService.saveEnvironmentData(microbeEnvironmentReqDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("환경 데이터 저장 성공")
                        .build());
    }

    @PostMapping("/records")
    public ResponseEntity<ResponseDTO> saveFoodWithImage(
            @RequestPart("microbeReqDTO") MicrobeRecordReqDTO microbeRecordReqDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        microbeService.saveRecord(microbeRecordReqDTO, image);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("현재 미생물 상태 저장 성공")
                        .build());
    }

    @GetMapping("/environments")
    public ResponseEntity<ResponseDTO> getEnvironment(
            @RequestBody MicrobeReqDTO microbeReqDTO) {

        MicrobeEnvironmentResDTO environment = microbeService.getEnvironment(microbeReqDTO.getMicrobeId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("현재 환경 정보 조회 성공")
                        .data(environment)
                        .build());
    }

    @GetMapping("/info")
    public ResponseEntity<ResponseDTO> getMicrobeInfo(
            @RequestBody MicrobeReqDTO microbeReqDTO) {

        MicrobeInfoResDTO microbeInfo = microbeService.getMicrobeInfo(microbeReqDTO.getMicrobeId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("미생물 정보 조회 성공")
                        .data(microbeInfo)
                        .build());
    }

}
