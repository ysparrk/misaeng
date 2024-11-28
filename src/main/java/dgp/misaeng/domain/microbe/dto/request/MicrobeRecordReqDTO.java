package dgp.misaeng.domain.microbe.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import dgp.misaeng.global.util.enums.MicrobeSoilCondition;
import lombok.Getter;

import java.util.List;

@Getter
public class MicrobeRecordReqDTO {
    private String serialNum;
    private List<String> foodCategory;
    private float weight;
    private String rgbStat;
    private MicrobeSoilCondition microbeSoilCondition;

    @JsonProperty("isEmpty")
    private boolean isEmpty;

    MicrobeRecordReqDTO() {}
}
