package dgp.misaeng.domain.capsule.dto.request;

import dgp.misaeng.global.util.enums.CapsuleType;
import lombok.Getter;

@Getter
public class CapsuleUseReqDTO {
    private CapsuleType capsuleType;
    private Integer capsuleCnt;
    private boolean useType;
}
