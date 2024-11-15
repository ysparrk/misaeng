package dgp.misaeng.global.auth.oauth.service;

import dgp.misaeng.global.auth.dto.OAuthUserInfo;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
@Slf4j
public enum OAuthProvider {
    KAKAO("kakao") {
        @Override
        public OAuthUserInfo toUserInfo(OAuth2User oauth2User) {
            Map<String, Object> attributes = oauth2User.getAttributes();
            Map<String, Object> properties = oauth2User.getAttribute("properties");
            Map<String, Object> account = oauth2User.getAttribute("kakao_account");
            log.info(attributes.toString());
            log.info(properties.toString());
            log.info(account.toString());
            return OAuthUserInfo.builder()
                    .provider(KAKAO.name)
                    .profileUrl(String.valueOf(properties.get("profile_image")))
                    .email(String.valueOf(account.get("email")))
                    .nickname(String.valueOf(properties.get("nickname")))
                    .oauthId(String.valueOf(attributes.get("id")))
                    .build();
        }
    };

    private static final Map<String, OAuthProvider> PROVIDERS =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(OAuthProvider::getName, Function.identity())));

    private final String name;

    public static OAuthProvider getOAuthProviderByName(String providerName) {
        if (!PROVIDERS.containsKey(providerName)) {
            throw new IllegalArgumentException("지원하지 않는 로그인입니다.");
        }
        return PROVIDERS.get(providerName);
    }

    public abstract OAuthUserInfo toUserInfo(OAuth2User oauth2User);
}
