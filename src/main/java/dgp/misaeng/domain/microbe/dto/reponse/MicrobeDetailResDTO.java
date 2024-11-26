package dgp.misaeng.domain.microbe.dto.reponse;

import dgp.misaeng.global.util.enums.MicrobeState;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class MicrobeDetailResDTO {
    private String time;
    private MicrobeState calendarState;
    private List<String> foodCategory;
    private float weight;
    private String imgUrl;
    private String timestamp;

    @Builder
    public MicrobeDetailResDTO(String time, MicrobeState calendarState, List<String> foodCategory, float weight, String imgUrl, String timestamp) {
        this.time = time;
        this.calendarState = calendarState;
        this.foodCategory = foodCategory;
        this.weight = weight;
        this.imgUrl = imgUrl;
        this.timestamp = timestamp;
    }
}
