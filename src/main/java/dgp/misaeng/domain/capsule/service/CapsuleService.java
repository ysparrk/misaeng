package dgp.misaeng.domain.capsule.service;

import dgp.misaeng.domain.capsule.dto.request.CapsuleReqDTO;
import dgp.misaeng.domain.capsule.dto.response.CapsuleResDTO;

public interface CapsuleService {
    CapsuleResDTO useCapsule(CapsuleReqDTO capsuleReqDTO);
    CapsuleResDTO getCapsule(String serialNum);
}
