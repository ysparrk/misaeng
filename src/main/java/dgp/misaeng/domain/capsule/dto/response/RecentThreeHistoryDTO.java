package dgp.misaeng.domain.capsule.dto.response;

import dgp.misaeng.global.util.enums.CapsuleType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class RecentThreeHistoryDTO {
    private CapsuleType capsuleType;
    private LocalDate date;

    @Builder
    public RecentThreeHistoryDTO(CapsuleType capsuleType, LocalDate date) {
        this.capsuleType = capsuleType;
        this.date = date;
    }
}
