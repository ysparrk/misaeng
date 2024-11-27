package dgp.misaeng.domain.device.service;

import dgp.misaeng.domain.device.dto.reponse.DeviceResDTO;
import dgp.misaeng.domain.device.dto.reponse.DeviceStateResDTO;
import dgp.misaeng.domain.device.dto.request.DeviceModeReqDTO;
import dgp.misaeng.domain.device.dto.request.DeviceReqDTO;
import dgp.misaeng.domain.device.entity.Device;
import dgp.misaeng.domain.device.entity.DeviceState;
import dgp.misaeng.domain.device.repository.DeviceRepository;
import dgp.misaeng.domain.device.repository.DeviceStateRepository;
import dgp.misaeng.domain.member.entity.Member;
import dgp.misaeng.domain.member.repository.MemberRepository;
import dgp.misaeng.domain.microbe.dto.reponse.MicrobeInfoResDTO;
import dgp.misaeng.domain.microbe.entity.Microbe;
import dgp.misaeng.domain.microbe.repository.MicrobeRepository;
import dgp.misaeng.domain.microbe.service.MicrobeService;
import dgp.misaeng.global.exception.CustomException;
import dgp.misaeng.global.exception.ErrorCode;
import dgp.misaeng.global.util.enums.DeviceMode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DeviceServiceImpl implements DeviceService {

    private final MemberRepository memberRepository;
    private final DeviceRepository deviceRepository;
    private final MicrobeRepository microbeRepository;
    private final DeviceStateRepository deviceStateRepository;
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
                .isDeleted(false)
                .build();

        deviceRepository.save(device);

        //초기값 설정
        DeviceState deviceState = DeviceState.builder()
                .device(device)
                .deviceMode(DeviceMode.GENERAL)
                .emptyState(false)
                .emptyActiveTime(12)
                .capsuleCycle(6)
                .closeWaitTime(5)
                .build();

        deviceStateRepository.save(deviceState);

        Microbe microbe = Microbe.builder()
                .device(device)
                .microbeName(deviceReqDTO.getMicrobeName())
                .survive(true)
                .isDeleted(false)
                .build();

        microbeRepository.save(microbe);
    }

    @Override
    public List<DeviceResDTO> getDevices(Long memberId) {
        // 1. 해당 member의 모든 device 조회
        List<Device> devices = deviceRepository.findAllByMemberId(memberId);

        // 결과 리스트 생성
        List<DeviceResDTO> deviceResDTOList = new ArrayList<>();

        // 2. 각 device에 연결된 microbe 조회 및 DTO 생성
        for (Device device : devices) {
            MicrobeInfoResDTO microbeInfo = null;

            // device에 연결된 microbe 조회
            Optional<Microbe> microbeOpt = microbeRepository.findByDeviceId(device.getDeviceId());
            if (microbeOpt.isPresent()) {
                Microbe microbe = microbeOpt.get();
                System.out.println(microbe.getMicrobeName());
                microbeInfo = microbeService.getMicrobeInfo(microbe.getMicrobeId());
            }

            // DeviceResDTO 생성 및 리스트에 추가
            DeviceResDTO deviceResDTO = DeviceResDTO.builder()
                    .deviceId(device.getDeviceId())
                    .deviceName(device.getDeviceName())
                    .microbeInfo(microbeInfo)
                    .build();

            deviceResDTOList.add(deviceResDTO);
        }

        return deviceResDTOList;
    }

    @Override
    public void setEmptyState(String serialNum, boolean emptyState) {
        DeviceState deviceState = deviceStateRepository.findBySerialNum(serialNum).orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_DEVICE_STATE) {
            @Override
            public ErrorCode getErrorCode() {
                return super.getErrorCode();
            }
        });

        deviceState.setEmptyState(emptyState);
        deviceStateRepository.save(deviceState);
    }

    @Transactional
    @Override
    public void updateDeviceMode(Long deviceId, DeviceMode deviceMode) {

        DeviceState deviceState = deviceStateRepository.findByDeviceId(deviceId).orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_DEVICE_STATE) {
            @Override
            public ErrorCode getErrorCode() {
                return super.getErrorCode();
            }
        });

        deviceState.setDeviceMode(deviceMode);

        deviceStateRepository.save(deviceState);
    }

    @Transactional
    @Override
    public void updateCapsuleCycle(Long deviceId, Integer time) {
        DeviceState deviceState = deviceStateRepository.findByDeviceId(deviceId).orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_DEVICE_STATE) {
            @Override
            public ErrorCode getErrorCode() {
                return super.getErrorCode();
            }
        });

        deviceState.setCapsuleCycle(time);
        deviceStateRepository.save(deviceState);
    }

    @Transactional
    @Override
    public void updateEmptyActiveTime(Long deviceId, Integer time) {
        DeviceState deviceState = deviceStateRepository.findByDeviceId(deviceId).orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_DEVICE_STATE) {
            @Override
            public ErrorCode getErrorCode() {
                return super.getErrorCode();
            }
        });

        deviceState.setEmptyActiveTime(time);
        deviceStateRepository.save(deviceState);
    }

    @Transactional
    @Override
    public void updateCloseWaitTime(Long deviceId, float second) {
        DeviceState deviceState = deviceStateRepository.findByDeviceId(deviceId).orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_DEVICE_STATE) {
            @Override
            public ErrorCode getErrorCode() {
                return super.getErrorCode();
            }
        });

        deviceState.setCloseWaitTime(second);
        deviceStateRepository.save(deviceState);
    }

    @Override
    public DeviceStateResDTO getDeviceState(String serialNum) {
        DeviceState deviceState = deviceStateRepository.findBySerialNum(serialNum).orElseThrow(() -> new CustomException(ErrorCode.NO_SUCH_DEVICE_STATE) {
            @Override
            public ErrorCode getErrorCode() {
                return super.getErrorCode();
            }
        });

        DeviceStateResDTO deviceStateResDTO = DeviceStateResDTO.builder()
                .deviceMode(deviceState.getDeviceMode())
                .emptyState(deviceState.getEmptyState())
                .emptyActiveTime(deviceState.getEmptyActiveTime())
                .capsuleCycle(deviceState.getCapsuleCycle())
                .closeWaitTime(deviceState.getCloseWaitTime())
                .build();

        return deviceStateResDTO;
    }
}
