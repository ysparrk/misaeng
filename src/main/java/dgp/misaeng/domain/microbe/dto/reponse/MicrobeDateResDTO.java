package dgp.misaeng.domain.microbe.dto.reponse;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class MicrobeDateResDTO {
    private LocalDate date;
    private List<MicrobeDetailResDTO> detailList;

    @Builder
    public MicrobeDateResDTO(LocalDate date, List<MicrobeDetailResDTO> detailList) {
        this.date = date;
        this.detailList = detailList;
    }
}
