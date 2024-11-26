package dgp.misaeng.domain.microbe.dto.reponse;

import dgp.misaeng.global.util.enums.FoodWeightState;
import dgp.misaeng.global.util.enums.MicrobeColor;
import dgp.misaeng.global.util.enums.MicrobeMood;
import dgp.misaeng.global.util.enums.MicrobeMessage;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MicrobeInfoResDTO {
    private Long microbeId;
    private String microbeName;
    private Integer bDay;
    private MicrobeColor microbeColor;
    private MicrobeMood microbeMood;
    private MicrobeMessage microbeMessage;
    private FoodWeightState foodWeightState;
    private float weight;
    private boolean forbidden;

    @Builder
    public MicrobeInfoResDTO(Long microbeId, String microbeName, Integer bDay, MicrobeColor microbeColor, MicrobeMood microbeMood, MicrobeMessage microbeMessage, FoodWeightState foodWeightState, float weight, boolean forbidden) {
        this.microbeId = microbeId;
        this.microbeName = microbeName;
        this.bDay = bDay;
        this.microbeColor = microbeColor;
        this.microbeMood = microbeMood;
        this.microbeMessage = microbeMessage;
        this.foodWeightState = foodWeightState;
        this.weight = weight;
        this.forbidden = forbidden;
    }
}
