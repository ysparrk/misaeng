package dgp.misaeng.domain.environment.entity;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "environment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE environment SET deleted_at = now() WHERE environment_id = ?")
@Where(clause = "is_deleted = false")
public class Environment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "environment", updatable = false)
    private Long environmentId;

    @Column(name = "temperature")
    private float temperature;

    @Column(name = "humidity")
    private float humidity;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public Environment(Long environmentId, float temperature, float humidity, LocalDateTime createdAt) {
        this.environmentId = environmentId;
        this.temperature = temperature;
        this.humidity = humidity;
        this.createdAt = createdAt;
    }
}
