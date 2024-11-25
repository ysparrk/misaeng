package dgp.misaeng.domain.microbe.dto.reponse;

import dgp.misaeng.global.util.enums.CalendarState;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class MicrobeDetailResDTO {
    private String time;
    private CalendarState calendarState;
    private List<String> foodCategory;
    private float weight;
    private String imgUrl;
    private String timestamp;

    @Builder
    public MicrobeDetailResDTO(String time, CalendarState calendarState, List<String> foodCategory, float weight, String imgUrl, String timestamp) {
        this.time = time;
        this.calendarState = calendarState;
        this.foodCategory = foodCategory;
        this.weight = weight;
        this.imgUrl = imgUrl;
        this.timestamp = timestamp;
    }
}
