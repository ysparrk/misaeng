package dgp.misaeng.domain.microbe.dto.reponse;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

public class MicrobeDateResDTO {
    private LocalDate date;
    private List<MicrobeDetailResDTO> detailList;

    @Builder
    public MicrobeDateResDTO(LocalDate date, List<MicrobeDetailResDTO> detailList) {
        this.date = date;
        this.detailList = detailList;
    }
}
