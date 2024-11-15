package dgp.misaeng.domain.member.service;

import dgp.misaeng.global.auth.dto.AuthUserInfo;
import dgp.misaeng.global.auth.dto.OAuthUserInfo;

public interface MemberService {
    public AuthUserInfo getOrRegisterUser(OAuthUserInfo oauthUserInfo);
}
