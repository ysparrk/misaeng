package dgp.misaeng.domain.microbe.service;

import dgp.misaeng.domain.microbe.dto.request.MicrobeRecordReqDTO;
import org.springframework.web.multipart.MultipartFile;

public interface MicrobeService {
    void saveRecord(MicrobeRecordReqDTO microbeRecordReqDTO, MultipartFile image);
}
