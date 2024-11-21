package dgp.misaeng.domain.food.controller;

import dgp.misaeng.domain.food.dto.request.FoodReqDTO;
import dgp.misaeng.global.dto.ResponseDTO;
import dgp.misaeng.global.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/foods")
@RestController
@RequiredArgsConstructor
public class FoodController {

    private final RedisService redisService;

    @PostMapping
    public ResponseEntity<ResponseDTO> saveFood(
            @RequestBody FoodReqDTO foodReqDTO) {

        redisService.saveFoodData(foodReqDTO);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("투여 음식 정보 저장 성공")
                        .build());
    }
}
