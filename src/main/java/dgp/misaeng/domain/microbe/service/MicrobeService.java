package dgp.misaeng.domain.microbe.service;

import dgp.misaeng.domain.microbe.dto.reponse.MicrobeEnvironmentResDTO;
import dgp.misaeng.domain.microbe.dto.reponse.MicrobeInfoResDTO;
import dgp.misaeng.domain.microbe.dto.request.MicrobeNewReqDTO;
import dgp.misaeng.domain.microbe.dto.request.MicrobeRecordReqDTO;
import dgp.misaeng.domain.microbe.dto.request.MicrobeUpdateReqDTO;
import org.springframework.web.multipart.MultipartFile;

public interface MicrobeService {
    void saveRecord(MicrobeRecordReqDTO microbeRecordReqDTO, MultipartFile image);
    MicrobeEnvironmentResDTO getEnvironment(Long microbeId);
    MicrobeInfoResDTO getMicrobeInfo(Long microbeId);
    void saveMicrobe(MicrobeNewReqDTO microbeNewReqDTO);
    void updateMicrobe(MicrobeUpdateReqDTO microbeUpdateReqDTO);
    void deleteMicrobe(Long microbeId);
    void expireMicrobe(Long microbeId);
}
