package dgp.misaeng.domain.microbe.service;

import dgp.misaeng.domain.microbe.dto.request.MicrobeRecordReqDTO;
import dgp.misaeng.domain.microbe.repository.MicrobeRepository;
import dgp.misaeng.global.exception.CustomException;
import dgp.misaeng.global.exception.ErrorCode;
import dgp.misaeng.global.service.RedisService;
import dgp.misaeng.global.service.S3ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MicrobeServiceImpl implements MicrobeService {

    private final MicrobeRepository microbeRepository;
    private final S3ImageService s3ImageService;
    private final RedisService redisService;

    @Override
    public void saveRecord(MicrobeRecordReqDTO microbeRecordReqDTO, MultipartFile image) {

        Long microbeId = microbeRepository.findMicrobeIdBySerialNum(microbeRecordReqDTO.getSericalNum()).orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_MICROBE) {
            @Override
            public ErrorCode getErrorCode() {
                return super.getErrorCode();
            }
        });

        String imgUrl = s3ImageService.upload(image);

        redisService.saveMicrobeRecordData(microbeId, microbeRecordReqDTO, imgUrl);
    }
}
