package dgp.misaeng.domain.capsule.dto.response;

import dgp.misaeng.global.util.enums.CapsuleType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CapsuleRemainResDTO {
    private CapsuleType capsuleType;
    private Integer remain;

    @Builder
    public CapsuleRemainResDTO(CapsuleType capsuleType, Integer remain) {
        this.capsuleType = capsuleType;
        this.remain = remain;
    }
}
