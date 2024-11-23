package dgp.misaeng.domain.microbe.service;

import dgp.misaeng.domain.microbe.dto.reponse.MicrobeEnvironmentResDTO;
import dgp.misaeng.domain.microbe.dto.reponse.MicrobeInfoResDTO;
import dgp.misaeng.domain.microbe.dto.request.MicrobeRecordReqDTO;
import org.springframework.web.multipart.MultipartFile;

public interface MicrobeService {
    void saveRecord(MicrobeRecordReqDTO microbeRecordReqDTO, MultipartFile image);
    MicrobeEnvironmentResDTO getEnvironment(Long microbeId);
    MicrobeInfoResDTO getMicrobeInfo(Long microbeId);
}
