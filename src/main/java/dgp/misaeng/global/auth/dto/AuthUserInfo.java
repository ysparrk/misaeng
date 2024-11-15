package dgp.misaeng.global.auth.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class AuthUserInfo {

    private Long id;
    private String email;
    private List<String> roles;

    public AuthUserInfo(){

    }

    public AuthUserInfo(Long id, String email, List<String> roles) {
        this.id = id;
        this.email = email;
        this.roles = roles;
    }
}
