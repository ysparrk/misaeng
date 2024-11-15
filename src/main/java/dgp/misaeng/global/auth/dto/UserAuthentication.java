package dgp.misaeng.global.auth.dto;

import lombok.Getter;

@Getter
public class UserAuthentication {
    private Long id;

    public UserAuthentication(Long id){
        this.id = id;
    }
}