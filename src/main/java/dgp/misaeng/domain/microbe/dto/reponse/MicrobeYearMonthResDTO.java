package dgp.misaeng.domain.microbe.dto.reponse;

import dgp.misaeng.global.util.enums.MicrobeState;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class MicrobeYearMonthResDTO {
    private LocalDate date;
    private MicrobeState calendarState;
}
