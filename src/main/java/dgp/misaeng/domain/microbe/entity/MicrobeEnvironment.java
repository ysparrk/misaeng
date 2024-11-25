package dgp.misaeng.domain.microbe.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(name = "microbe_environment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE microbe_environment SET deleted_at = now() WHERE microbe_environment_id = ?")
@Where(clause = "is_deleted = false")
public class MicrobeEnvironment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "microbe_environment_id", updatable = false)
    private Long microbeEnvironmentId;

    @Column(name = "temperature")
    private float temperature;

    @Column(name = "humidity")
    private float humidity;

    @ManyToOne(fetch = FetchType.LAZY)
    private Microbe microbe;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

}
