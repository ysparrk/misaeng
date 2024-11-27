package dgp.misaeng.domain.capsule.service;

import dgp.misaeng.domain.capsule.dto.request.CapsuleReqDTO;
import dgp.misaeng.domain.capsule.dto.request.CapsuleUseReqDTO;
import dgp.misaeng.domain.capsule.dto.response.CapsuleRemainResDTO;
import dgp.misaeng.domain.capsule.dto.response.CapsuleResDTO;
import dgp.misaeng.domain.capsule.dto.response.RecentThreeHistoryDTO;
import dgp.misaeng.domain.capsule.entity.Capsule;
import dgp.misaeng.domain.capsule.entity.CapsuleHistory;
import dgp.misaeng.domain.capsule.repository.CapsuleHistoryRepository;
import dgp.misaeng.domain.capsule.repository.CapsuleRepository;
import dgp.misaeng.domain.microbe.repository.MicrobeRepository;
import dgp.misaeng.global.exception.CustomException;
import dgp.misaeng.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CapsuleServiceImpl implements CapsuleService {

    private final CapsuleRepository capsuleRepository;
    private final CapsuleHistoryRepository capsuleHistoryRepository;
    private final MicrobeRepository microbeRepository;

    @Transactional
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
                    .useState(capsuleUseReqDTO.isUseType())
                    .build();
            capsuleHistoryRepository.save(capsuleHistory);
        }
    }

    @Override
    public CapsuleResDTO getCapsule(String serialNum) {
        // 1. 캡슐 잔여량 조회
        List<CapsuleRemainResDTO> capsuleRemainList = capsuleRepository.findAllBySerialNum(serialNum).stream()
                .map(capsule -> {
                    return new CapsuleRemainResDTO(
                            capsule.getCapsuleType(),
                            capsule.getStock()
                    );
                })
                .collect(Collectors.toList());

        // 2. 최근 3개의 히스토리 조회
        Long microbeId = microbeRepository.findMicrobeIdBySerialNum(serialNum).orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_MICROBE) {
            @Override
            public ErrorCode getErrorCode() {
                return super.getErrorCode();
            }
        });


        Pageable pageable = PageRequest.of(0, 3);
        List<RecentThreeHistoryDTO> recentThreeHistoryList = capsuleHistoryRepository.findRecentThreeByMicrobeId(microbeId, pageable).stream()
                .map(history -> {
                    return new RecentThreeHistoryDTO(
                            history.getCapsule().getCapsuleType(),
                            history.getCreatedAt().toLocalDate()
                    );
                })
                .collect(Collectors.toList());

        // 3. result
        CapsuleResDTO response = CapsuleResDTO.builder()
                .capsuleRemain(capsuleRemainList)
                .recentThreeHistory(recentThreeHistoryList)
                .build();

        return response;

    }
}
