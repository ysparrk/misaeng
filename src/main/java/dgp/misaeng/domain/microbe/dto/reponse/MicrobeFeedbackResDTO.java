package dgp.misaeng.domain.microbe.dto.reponse;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class MicrobeFeedbackResDTO {
    private java.time.LocalDate date;
    private List<MicrobeFeedbackDetailResDTO> dataList;

    @Builder
    public MicrobeFeedbackResDTO(LocalDate date, List<MicrobeFeedbackDetailResDTO> dataList) {
        this.date = date;
        this.dataList = dataList;
    }
}
