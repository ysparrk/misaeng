package dgp.misaeng.domain.microbe.entity;

import dgp.misaeng.domain.device.entity.Device;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "microbe")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE microbe SET deleted_at = now() WHERE id = ?")
@Where(clause = "is_deleted = false")
public class Microbe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long microbeId;

    @Column(name = "microbe_name", length = 30, nullable = false)
    private String microbeName;

    @Column(name = "survive", nullable = false)
    @ColumnDefault("true")
    private Boolean survive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

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
    public Microbe(Long microbeId, String microbeName, Boolean survive, Device device, LocalDateTime createdAt, LocalDateTime modifiedAt, Boolean isDeleted, LocalDateTime deletedAt) {
        this.microbeId = microbeId;
        this.microbeName = microbeName;
        this.survive = survive;
        this.device = device;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.isDeleted = isDeleted;
        this.deletedAt = deletedAt;
    }

    public void setMicrobeName(String microbeName) {
        this.microbeName = microbeName;
    }

    public void setSurvive(Boolean survive) {
        this.survive = survive;
    }

    public void setDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
        this.deletedAt = LocalDateTime.now();
    }
}
