package dgp.misaeng.domain.microbe.dto.reponse;

import dgp.misaeng.global.util.enums.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class MicrobeInfoResDTO {
    private Long microbeId;
    private String microbeName;
    private Integer bDay;
    private MicrobeColor microbeColor;
    private MicrobeMood microbeMood;
    private MicrobeMessage microbeMessage;
    private FoodWeightState foodWeightState;
    private MicrobeState microbeState;
    private float weight;
    private boolean forbidden;
    private LocalDate createdAt;

    @Builder
    public MicrobeInfoResDTO(Long microbeId, String microbeName, Integer bDay, MicrobeColor microbeColor, MicrobeMood microbeMood, MicrobeMessage microbeMessage, FoodWeightState foodWeightState, MicrobeState microbeState, float weight, boolean forbidden, LocalDate createdAt) {
        this.microbeId = microbeId;
        this.microbeName = microbeName;
        this.bDay = bDay;
        this.microbeColor = microbeColor;
        this.microbeMood = microbeMood;
        this.microbeMessage = microbeMessage;
        this.foodWeightState = foodWeightState;
        this.microbeState = microbeState;
        this.weight = weight;
        this.forbidden = forbidden;
        this.createdAt = createdAt;
    }
}
