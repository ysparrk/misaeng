package dgp.misaeng.domain.capsule.service;

import dgp.misaeng.domain.capsule.dto.request.CapsuleReqDTO;
import dgp.misaeng.domain.capsule.dto.request.CapsuleUseReqDTO;
import dgp.misaeng.domain.capsule.entity.Capsule;
import dgp.misaeng.domain.capsule.entity.CapsuleHistory;
import dgp.misaeng.domain.capsule.repository.CapsuleHistoryRepository;
import dgp.misaeng.domain.capsule.repository.CapsuleRepository;
import dgp.misaeng.global.exception.CustomException;
import dgp.misaeng.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CapsuleServiceImpl implements CapsuleService {

    private static CapsuleRepository capsuleRepository;
    private static CapsuleHistoryRepository capsuleHistoryRepository;

    @Override
    public void useCapsule(CapsuleReqDTO capsuleReqDTO) {

        List<Capsule> capsuleList = capsuleRepository.findAllBySerialNum(capsuleReqDTO.getSerialNum());

        for (CapsuleUseReqDTO capsuleUseReqDTO : capsuleReqDTO.getCapsuleList()) {

            Capsule capsule = capsuleList.stream()
                    .filter(c -> c.getCapsuleType().equals(capsuleUseReqDTO.getCapsuleType()))
                    .findFirst()
                    .orElseThrow(() -> new CustomException(ErrorCode.CAPSULE_NOT_FOUND) {
                        @Override
                        public ErrorCode getErrorCode() {
                            return super.getErrorCode();
                        }
                    });

            //useType -> true : 재고 감소, false -> 재고 더하기
            if (capsuleUseReqDTO.isUseType()) capsule.setStock(capsule.getStock() - capsuleUseReqDTO.getCapsuleCnt());
            else capsule.setStock(capsule.getStock() + capsuleUseReqDTO.getCapsuleCnt());

            CapsuleHistory capsuleHistory = CapsuleHistory.builder()
                    .capsule(capsule)
                    .useCnt(capsuleUseReqDTO.getCapsuleCnt())
                    .build();
            capsuleHistoryRepository.save(capsuleHistory);
        }
    }
}
