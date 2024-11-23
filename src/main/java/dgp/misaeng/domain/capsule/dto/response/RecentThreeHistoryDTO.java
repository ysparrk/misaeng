package dgp.misaeng.domain.capsule.dto.response;

import dgp.misaeng.global.util.enums.CapsuleType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RecentThreeHistoryDTO {
    private CapsuleType capsuleType;
    private LocalDateTime date;

    @Builder
    public RecentThreeHistoryDTO(CapsuleType capsuleType, LocalDateTime date) {
        this.capsuleType = capsuleType;
        this.date = date;
    }
}
