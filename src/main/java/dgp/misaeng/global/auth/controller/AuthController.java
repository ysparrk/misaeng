package dgp.misaeng.global.auth.controller;

import dgp.misaeng.global.auth.dto.ReissueTokenReqDTO;
import dgp.misaeng.global.auth.dto.ReissueTokenResDTO;
import dgp.misaeng.global.auth.service.JwtService;
import dgp.misaeng.global.dto.ResponseDTO;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;

    // Access Token 재발급
    @PostMapping("/reissue")
    public ResponseEntity<ResponseDTO> reissue(
            @CookieValue(name = "refreshToken") Cookie cookie,
            @RequestBody ReissueTokenReqDTO reissueTokenReqDTO) {
        String refreshToken = cookie.getValue();
        String newAccessToken = jwtService.reissueAccessToken(refreshToken, reissueTokenReqDTO);
        ReissueTokenResDTO reissueTokenResDTO = ReissueTokenResDTO.builder()
                .accessToken(newAccessToken).build();

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseDTO.builder()
                        .message("Access Token 재발급 성공")
                        .data(reissueTokenResDTO)
                        .build());
    }
}
