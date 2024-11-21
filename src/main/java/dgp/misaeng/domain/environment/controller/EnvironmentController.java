package dgp.misaeng.domain.environment.controller;

import dgp.misaeng.domain.environment.dto.request.EnvironmentReqDTO;
import dgp.misaeng.global.dto.ResponseDTO;
import dgp.misaeng.global.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/environments")
@RestController
@RequiredArgsConstructor
public class EnvironmentController {

    private final RedisService redisService;

    @PostMapping
    public ResponseEntity<ResponseDTO> saveEnvironment(
            @RequestBody EnvironmentReqDTO environmentReqDTO) {

        redisService.saveEnvironmentData(environmentReqDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("환경 데이터 저장 성공")
                        .build());
    }
}
