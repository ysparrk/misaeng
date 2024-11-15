package dgp.misaeng.global.auth.service;

import dgp.misaeng.global.auth.dto.ReissueTokenReqDTO;
import dgp.misaeng.global.auth.exception.TokenException;
import dgp.misaeng.global.exception.ErrorCode;
import dgp.misaeng.global.service.RedisService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;


@Service
public class JwtService {
    private final RedisService redisService;
    private static final long MILLI_SECOND = 1000L;
    private final String secretKey;

    private final long expirationHours = 60 * 30;
    private final String issuer;


    //30일
    private final long refreshTokenExpire = 60 * 60 * 24 * 30;

    public JwtService(
            RedisService redisService,
            @Value("${custom.jwt.issuer}") String issuer,
            @Value("${custom.jwt.secretKey}") String secretKey
    ) {
        this.redisService = redisService;
        this.issuer=issuer;
        this.secretKey=secretKey;
    }

    public String createAccessToken(Long userSpecification){
        Date now = new Date();
        Date validity = new Date(now.getTime() + expirationHours * MILLI_SECOND);
        System.out.println("secretKey = " + secretKey);
        return Jwts.builder()
                .signWith((new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName()))) //HS512 알고리즘 이용, secretKey 이용
                .setSubject(String.valueOf(userSpecification)) //토큰에 담을 정보
                .setIssuer(issuer) //발급자
                .setIssuedAt(now) //발급시간
                .setExpiration(validity) //만료시간
                .compact();
    }

    public String createRefreshToken(){
        Date now = new Date();
        Date validity = new Date(now.getTime() + refreshTokenExpire * MILLI_SECOND);

        return Jwts.builder()
                .signWith((new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName()))) //HS512 알고리즘 이용, secretKey 이용
                .setIssuer(issuer)
                .setIssuedAt(now)
                .setExpiration(validity)
                .compact();
    }

    public String reissueAccessToken(String refreshToken, ReissueTokenReqDTO reissueTokenReqDTO){
        // refreshToken 확인
        validateToken(refreshToken);
        String accessToken = reissueTokenReqDTO.getAccessToken();
        Long id = Long.valueOf(extractSubFromToken(accessToken));
        System.out.println(id);
        String redisRefreshToken = redisService.getRefreshToken(String.valueOf(id));

        // 기존 refreshToken 일치 확인
        if (!Objects.equals(redisRefreshToken, refreshToken)){
            throw new TokenException(ErrorCode.TOKEN_INVALID);
        }

        return createAccessToken(id);
    }

    public String getSubject(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public void validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token);
        }catch(ExpiredJwtException e){
            throw new TokenException(ErrorCode.TOKEN_EXPIRED);
        }catch(JwtException | IllegalArgumentException | NullPointerException e){
            throw new TokenException(ErrorCode.TOKEN_INVALID);
        }
    }

    public int getRefreshTokenExpire() {
        return (int) refreshTokenExpire;
    }

    public String extractSubFromToken(String expiredAccessToken) {
        JSONParser jsonParser = new JSONParser();
        String[] split = expiredAccessToken.split("\\.");
        byte[] payLoad = split[1].getBytes();
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] userId = decoder.decode(payLoad);
        JSONObject jsonObject = null;
        try{
            jsonObject = (JSONObject) jsonParser.parse(new String(userId));
        }catch (Exception e){
            e.printStackTrace();
            throw new TokenException(ErrorCode.TOKEN_INVALID);
        }
        return (String) jsonObject.get("sub");
    }
}
