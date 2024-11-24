package dgp.misaeng.domain.microbe.controller;

import dgp.misaeng.domain.microbe.dto.reponse.MicrobeDateResDTO;
import dgp.misaeng.domain.microbe.dto.reponse.MicrobeEnvironmentResDTO;
import dgp.misaeng.domain.microbe.dto.reponse.MicrobeInfoResDTO;
import dgp.misaeng.domain.microbe.dto.reponse.MicrobeYearMonthResDTO;
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

    @PostMapping
    public ResponseEntity<ResponseDTO> saveNewMicrobe(
            @RequestBody MicrobeNewReqDTO microbeNewReqDTO
    ) {
        microbeService.saveMicrobe(microbeNewReqDTO);

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

    @GetMapping
    public ResponseEntity<ResponseDTO> getMicrobesYearMonth(
            @RequestParam YearMonth yearMonth,
            @RequestBody MicrobeReqDTO microbeReqDTO
            ) {
        List<MicrobeYearMonthResDTO> calendar = microbeService.getYearMonth(microbeReqDTO.getMicrobeId(), yearMonth);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("음식물 투여 캘린더 조회 성공")
                        .data(calendar)
                        .build());
    }

    @GetMapping("/details")
    public ResponseEntity<ResponseDTO> getMicrobesYearMonth(
            @RequestParam LocalDate localDate,
            @RequestBody MicrobeReqDTO microbeReqDTO
    ) {

        MicrobeDateResDTO dateDetails = microbeService.getDateDetails(microbeReqDTO.getMicrobeId(), localDate);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("음식물 투여 캘린더 디테일 조회 성공")
                        .data(dateDetails)
                        .build());
    }
}
