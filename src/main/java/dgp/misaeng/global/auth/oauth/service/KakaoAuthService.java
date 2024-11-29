package dgp.misaeng.global.auth.oauth.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dgp.misaeng.global.auth.dto.KakaoUserResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KakaoAuthService {
    public KakaoUserResponse getUserInfo(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    String.class // Raw JSON 응답 받기
            );

            System.out.println("Kakao API Raw Response: " + response.getBody());

            // HTTP 상태 코드 확인
            if (response.getStatusCode() != HttpStatus.OK) {
                System.err.println("Failed to get user info. HTTP Status: " + response.getStatusCode());
                return null;
            }

            // JSON 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            KakaoUserResponse userInfo = objectMapper.readValue(response.getBody(), KakaoUserResponse.class);

            return userInfo;

        } catch (Exception e) {
            System.err.println("Error while calling Kakao API: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}

