package dgp.misaeng.domain.microbe.dto.reponse;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class MicrobeFeedbackDetailResDTO {
    private List<String> foodCategory;
    private String imgUrl;
    private LocalDateTime createdAt;

    @Builder
    public MicrobeFeedbackDetailResDTO(List<String> foodCategory, String imgUrl, LocalDateTime createdAt) {
        this.foodCategory = foodCategory;
        this.imgUrl = imgUrl;
        this.createdAt = createdAt;
    }
}
