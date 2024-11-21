package dgp.misaeng.domain.food.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class FoodReqDTO {
    private String sericalNum;
    private List<String> foodCategory;
    private float weight;
    private String rgbStat;

    public FoodReqDTO() {}

}
