package dgp.misaeng.domain.device.entity;

import dgp.misaeng.domain.member.entity.Member;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "device")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE device SET deleted_at = now() WHERE device_id = ?")
@Where(clause = "is_deleted = false")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "device_id", updatable = false)
    private Long deviceId;

    @Column(name = "serial_num", length = 30, nullable = false)
    private String serialNum;

    @Column(name = "device_type", length = 30, nullable = false)
    private String deviceType;

    @Column(name = "device_name", length = 30)
    private String deviceName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedAt;

    @ColumnDefault("false")
    @Column(name = "is_deleted")
    private Boolean isDeleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Builder
    public Device(Long deviceId, String serialNum, String deviceType, String deviceName, Member member, LocalDateTime createdAt, LocalDateTime modifiedAt, Boolean isDeleted, LocalDateTime deletedAt) {
        this.deviceId = deviceId;
        this.serialNum = serialNum;
        this.deviceType = deviceType;
        this.deviceName = deviceName;
        this.member = member;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.isDeleted = isDeleted;
        this.deletedAt = deletedAt;
    }
}
