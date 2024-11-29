package dgp.misaeng.domain.member.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberDetailResDTO {
    private String name;
    private String email;

    @Builder
    public MemberDetailResDTO(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
