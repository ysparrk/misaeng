package dgp.misaeng.global.auth.oauth.handler;

import dgp.misaeng.global.auth.repository.HttpCookieOAuth2AuthorizationRequestRepository;
import dgp.misaeng.global.util.cookie.CookieService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URLDecoder;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
@Slf4j
@RequiredArgsConstructor
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final CookieService cookieService;


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        log.debug(exception.getMessage());
        String redirect_uri = cookieService.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(cookie -> cookie.getValue())
                .map(cookie -> URLDecoder.decode(cookie, UTF_8))
                .orElse("/");

        String targetUrl = UriComponentsBuilder.fromUriString(redirect_uri)
                .queryParam("error", "error")
                .build().toUriString();
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}