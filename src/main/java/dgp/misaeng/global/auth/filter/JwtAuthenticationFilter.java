package dgp.misaeng.global.auth.filter;

import dgp.misaeng.global.auth.dto.UserAuthentication;
import dgp.misaeng.global.auth.service.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TYPE = "Bearer";

    @Value("${custom.jwt.secretKey}")
    private String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        //헤더에서 AUTHORIZATION에 담긴 토큰 가져오기
        String token = parseBearerToken(request);

        //만약 토큰이 없다면 걍 넘김
        if(token==null){
            filterChain.doFilter(request,response);
            return;
        }

        // Kakao Access Token 검증
        if (!jwtService.validateKakaoAccessToken(token)) {
            System.out.println("Invalid Kakao access token");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid Kakao access token");
            response.getWriter().flush();
            return;
        }

        //Kakao Access Token이 유효하면 새로운 JWT 생성
        token = jwtService.generateJwtForKakaoUser(token);

        //user id 가져오기
        UserAuthentication userAuthentication = parseUserSpecification(token);

        //AuthenticationToken 제작
        List<String> roles = Arrays.asList("USER");
        List<SimpleGrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userAuthentication,null,authorities);
        //detail 설정
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request,response);

    }

    //새로 만듬
    // 토큰이 서버에서 발급한 JWT인지 확인하는 메서드
    private boolean isJwtToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private String parseBearerToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private UserAuthentication parseUserSpecification(String token) {

        Long id = Long.valueOf(Optional.ofNullable(token)
                .filter(subject -> subject.length() >= 1)
                .map(jwtService::getSubject)
                .orElse("null"));
        return new UserAuthentication(id);
    }


}
