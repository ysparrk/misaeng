package dgp.misaeng.domain.device.service;

import dgp.misaeng.domain.device.dto.reponse.DeviceResDTO;
import dgp.misaeng.domain.device.dto.request.DeviceReqDTO;
import dgp.misaeng.domain.device.entity.Device;
import dgp.misaeng.domain.device.repository.DeviceRepository;
import dgp.misaeng.domain.member.entity.Member;
import dgp.misaeng.domain.member.repository.MemberRepository;
import dgp.misaeng.domain.microbe.dto.reponse.MicrobeInfoResDTO;
import dgp.misaeng.domain.microbe.entity.Microbe;
import dgp.misaeng.domain.microbe.repository.MicrobeRepository;
import dgp.misaeng.domain.microbe.service.MicrobeService;
import dgp.misaeng.global.exception.CustomException;
import dgp.misaeng.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeviceServiceImpl implements DeviceService {

    private final MemberRepository memberRepository;
    private final DeviceRepository deviceRepository;
    private final MicrobeRepository microbeRepository;
    private final MicrobeService microbeService;


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

    @Override
    public List<DeviceResDTO> getDevices(Long memberId) {
        // 1. 해당 member의 모든 device 조회
        List<Device> devices = deviceRepository.findAllByMemberId(memberId);

        // 2. 각 device에 연결된 microbe 조회 및 DTO 생성
        return devices.stream()
                .map(device -> {
                    MicrobeInfoResDTO microbeInfo = null;

                    // device에 연결된 microbe 조회
                    Optional<Microbe> microbeOpt = microbeRepository.findByDeviceId(device.getDeviceId());
                    if (microbeOpt.isPresent()) {
                        Microbe microbe = microbeOpt.get();
                        microbeInfo = microbeService.getMicrobeInfo(microbe.getMicrobeId());
                    }

                    return DeviceResDTO.builder()
                            .deviceId(device.getDeviceId())
                            .deviceName(device.getDeviceName())
                            .microbeInfo(microbeInfo)
                            .build();
                })
                .collect(Collectors.toList());
    }
}
