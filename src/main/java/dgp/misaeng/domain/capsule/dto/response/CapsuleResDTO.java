package dgp.misaeng.domain.capsule.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class CapsuleResDTO {
    private List<CapsuleRemainResDTO> capsuleRemain;
    private List<RecentThreeHistoryDTO> recentThreeHistory;

    @Builder
    public CapsuleResDTO(List<CapsuleRemainResDTO> capsuleRemain, List<RecentThreeHistoryDTO> recentThreeHistory) {
        this.capsuleRemain = capsuleRemain;
        this.recentThreeHistory = recentThreeHistory;
    }
}
