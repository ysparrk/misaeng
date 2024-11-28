package dgp.misaeng.domain.microbe.dto.reponse;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class MicrobeDateResDTO {
    private LocalDate date;
    private float totalWeight;
    private List<MicrobeDetailResDTO> detailList;

    @Builder
    public MicrobeDateResDTO(LocalDate date, float totalWeight, List<MicrobeDetailResDTO> detailList) {
        this.date = date;
        this.totalWeight = totalWeight;
        this.detailList = detailList;
    }
}
