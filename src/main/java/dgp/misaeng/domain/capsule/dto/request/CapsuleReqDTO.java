package dgp.misaeng.domain.capsule.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class CapsuleReqDTO {
    private String serialNum;
    private List<CapsuleUseReqDTO> capsuleList;
}
