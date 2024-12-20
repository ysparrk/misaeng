package dgp.misaeng.domain.microbe.controller;

import dgp.misaeng.domain.microbe.dto.reponse.*;
import dgp.misaeng.domain.microbe.dto.request.*;
import dgp.misaeng.domain.microbe.service.MicrobeService;
import dgp.misaeng.global.dto.ResponseDTO;
import dgp.misaeng.global.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

@RequestMapping("/microbes")
@RestController
@RequiredArgsConstructor
public class MicrobeController {

    private final MicrobeService microbeService;
    private final RedisService redisService;

    @PostMapping("/environments")
    public ResponseEntity<ResponseDTO> saveEnvironment(
            @RequestBody MicrobeEnvironmentReqDTO microbeEnvironmentReqDTO) {

        redisService.saveEnvironmentData(microbeEnvironmentReqDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("온도/습도/가스 데이터 저장 성공")
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

    @GetMapping("/environments/{microbeId}")
    public ResponseEntity<ResponseDTO> getEnvironment(
            @PathVariable Long microbeId
    ) {

        MicrobeEnvironmentResDTO environment = microbeService.getEnvironment(microbeId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("현재 온/습도 정보 조회 성공")
                        .data(environment)
                        .build());
    }

    @GetMapping("/info/{deviceId}/{microbeId}")
    public ResponseEntity<ResponseDTO> getMicrobeInfo(
            @PathVariable Long deviceId,
            @PathVariable Long microbeId
    ) {

        MicrobeInfoResDTO microbeInfo = microbeService.getMicrobeInfo(microbeId, deviceId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("미생물 정보 조회 성공")
                        .data(microbeInfo)
                        .build());
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> saveNewMicrobe(
            @RequestBody MicrobeNewReqDTO microbeNewReqDTO
    ) {
        microbeService.saveMicrobe(microbeNewReqDTO.getDeviceId(), microbeNewReqDTO.getMicrobeName());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("새로운 미생물 등록 성공")
                        .build());
    }

    @PutMapping
    public ResponseEntity<ResponseDTO> updateMicrobe(
            @RequestBody MicrobeUpdateReqDTO microbeUpdateReqDTO
    ) {

        microbeService.updateMicrobe(microbeUpdateReqDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("미생물 정보 수정 성공")
                        .build());
    }

    @DeleteMapping
    public ResponseEntity<ResponseDTO> deleteMicrobe(
            @RequestBody MicrobeReqDTO microbeReqDTO
    ) {

        microbeService.deleteMicrobe(microbeReqDTO.getMicrobeId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("미생물 삭제 성공")
                        .build());
    }

    @PutMapping("/expire")
    public ResponseEntity<ResponseDTO> expireMicrobe(
            @RequestBody MicrobeReqDTO microbeReqDTO
    ) {

        microbeService.expireMicrobe(microbeReqDTO.getMicrobeId());

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("미생물 만료 성공")
                        .build());
    }

    @GetMapping("/{microbeId}")
    public ResponseEntity<ResponseDTO> getMicrobesYearMonth(
            @PathVariable Long microbeId,
            @RequestParam YearMonth yearMonth
            ) {
        List<MicrobeYearMonthResDTO> calendar = microbeService.getYearMonth(microbeId, yearMonth);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("음식물 투여 캘린더 조회 성공")
                        .data(calendar)
                        .build());
    }

    @GetMapping("/detail/{microbeId}")
    public ResponseEntity<ResponseDTO> getMicrobesDateDetail(
            @RequestParam LocalDate localDate,
            @PathVariable Long microbeId
    ) {

        MicrobeDateResDTO dateDetails = microbeService.getDateDetails(microbeId, localDate);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("음식물 투여 캘린더 디테일 조회 성공")
                        .data(dateDetails)
                        .build());
    }

    @PutMapping("/update-detail")
    public ResponseEntity<ResponseDTO> updateMicrobesDateDetail(
        @RequestBody MicrobeDetailUpdateReqDTO microbeDetailUpdateReqDTO
    ) {
        microbeService.updateDateDetails(microbeDetailUpdateReqDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("음식물 투여 카테고리 디테일 수정 성공")
                        .build());
    }

    @GetMapping("/feedback/{serialNum}")
    public ResponseEntity<ResponseDTO> getMicrobesFeedback(
            @PathVariable String serialNum,
            @RequestParam LocalDate date
    ) {
        MicrobeFeedbackResDTO feedback = microbeService.getFeedback(serialNum, date);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("사용자 피드백을 위한 음식물 투여 데이터 조회 성공")
                        .data(feedback)
                        .build());
    }
}
