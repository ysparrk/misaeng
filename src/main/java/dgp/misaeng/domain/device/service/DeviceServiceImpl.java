package dgp.misaeng.domain.device.service;

import dgp.misaeng.domain.device.dto.request.DeviceReqDTO;
import dgp.misaeng.domain.device.entity.Device;
import dgp.misaeng.domain.device.repository.DeviceRepository;
import dgp.misaeng.domain.member.entity.Member;
import dgp.misaeng.domain.member.repository.MemberRepository;
import dgp.misaeng.domain.microbe.entity.Microbe;
import dgp.misaeng.domain.microbe.repository.MicrobeRepository;
import dgp.misaeng.global.exception.CustomException;
import dgp.misaeng.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeviceServiceImpl implements DeviceService {

    private final MemberRepository memberRepository;
    private final DeviceRepository deviceRepository;
    private final MicrobeRepository microbeRepository;


    @Override
    public void saveDevice(Long memberId, DeviceReqDTO deviceReqDTO) {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND) {
            @Override
            public ErrorCode getErrorCode() {
                return super.getErrorCode();
            }
        });

        Device device = Device.builder()
                .member(member)
                .serialNum(deviceReqDTO.getSerialNum())
                .deviceType(deviceReqDTO.getDeviceType())
                .deviceName(deviceReqDTO.getDeviceName())
                .build();

        deviceRepository.save(device);

        Microbe microbe = Microbe.builder()
                .device(device)
                .microbeName(deviceReqDTO.getMicrobeName())
                .build();

        microbeRepository.save(microbe);
    }
}
