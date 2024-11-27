package dgp.misaeng.domain.device.entity;

import dgp.misaeng.global.util.enums.DeviceMode;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;


@Entity
@Table(name = "device_state")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DeviceState {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long deviceStateId;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_mode")
    DeviceMode deviceMode;

    @Column(name = "empty_state")
    private Boolean emptyState;

    @Column(name = "empty_active_time")
    private Integer emptyActiveTime;

    @Column(name = "capsule_cycle")
    private Integer capsuleCycle;

    @Column(name = "close_wait_time")
    private float closeWaitTime;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @Builder
    public DeviceState(Long deviceStateId, DeviceMode deviceMode, Boolean emptyState, Integer emptyActiveTime, Integer capsuleCycle, float closeWaitTime, Device device) {
        this.deviceStateId = deviceStateId;
        this.deviceMode = deviceMode;
        this.emptyState = emptyState;
        this.emptyActiveTime = emptyActiveTime;
        this.capsuleCycle = capsuleCycle;
        this.closeWaitTime = closeWaitTime;
        this.device = device;
    }


    public void setDeviceMode(DeviceMode deviceMode) {
        this.deviceMode = deviceMode;
    }

    public void setEmptyState(Boolean emptyState) {
        this.emptyState = emptyState;
    }

    public void setEmptyActiveTime(Integer emptyActiveTime) {
        this.emptyActiveTime = emptyActiveTime;
    }

    public void setCapsuleCycle(Integer capsuleCycle) {
        this.capsuleCycle = capsuleCycle;
    }

    public void setCloseWaitTime(float closeWaitTime) {
        this.closeWaitTime = closeWaitTime;
    }
}
