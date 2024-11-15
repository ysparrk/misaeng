package dgp.misaeng.global.auth.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthUserInfo {

    private String profileUrl;
    private String email;
    private String nickname;
    private String provider;
    private String oauthId;

    public OAuthUserInfo(){

    }

    @Builder
    public OAuthUserInfo(String profileUrl, String email, String nickname, String provider, String oauthId) {
        this.profileUrl = profileUrl;
        this.email = email;
        this.nickname = nickname;
        this.provider = provider;
        this.oauthId = oauthId;
    }
}
