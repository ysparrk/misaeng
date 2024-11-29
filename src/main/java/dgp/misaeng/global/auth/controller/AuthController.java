package dgp.misaeng.global.auth.controller;

import dgp.misaeng.domain.member.service.MemberService;
import dgp.misaeng.global.auth.dto.*;
import dgp.misaeng.global.auth.oauth.service.KakaoAuthService;
import dgp.misaeng.global.auth.service.JwtService;
import dgp.misaeng.global.dto.ResponseDTO;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final KakaoAuthService kakaoAuthService;
    private final MemberService memberService;
    private final RestTemplate restTemplate = new RestTemplate();

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

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

    @GetMapping("/kakao-user")
    public ResponseEntity<KakaoUserResponse> getUserInfo(@RequestHeader("Authorization") String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken); // Bearer 포함

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserResponse> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                entity,
                KakaoUserResponse.class
        );

        return ResponseEntity.ok(response.getBody());
    }

    @PostMapping("/kakao")
    public ResponseEntity<?> authenticateWithKakao(@RequestHeader("Authorization") String authorizationHeader) {

        // 헤더에서 'Bearer ' 제거
        String accessToken = authorizationHeader.replace("Bearer ", "").trim();

        try {
            // Kakao API 호출
            KakaoUserResponse kakaoUser = kakaoAuthService.getUserInfo(accessToken);

            String email = kakaoUser.getKakaoAccount().getEmail();
            String nickname = kakaoUser.getKakaoAccount().getProfile().getNickname();

            // 사용자 정보를 저장하거나 가져오기
            OAuthUserInfo oAuthUserInfo = OAuthUserInfo.builder()
                    .oauthId(kakaoUser.getId().toString())
                    .nickname(nickname)
                    .email(email)
                    .build();
            AuthUserInfo authUserInfo = memberService.getOrRegisterUser(oAuthUserInfo);

            // JWT 생성
            String jwtToken = jwtService.makeAccessToken(kakaoUser.getId());

            logger.info("Authentication successful");
            return ResponseEntity.ok(jwtToken);
        } catch (Exception e) {
            logger.error("Authentication failed: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Authentication failed: " + e.getMessage());
        }
    }

}
