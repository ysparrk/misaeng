package dgp.misaeng.domain.microbe.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class MicrobeDetailUpdateReqDTO {
    private Long microbeId;
    private Long timestamp;
    private List<String> foodCategory;
}
