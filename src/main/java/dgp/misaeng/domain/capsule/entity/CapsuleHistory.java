package dgp.misaeng.domain.capsule.entity;

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
@Table(name = "capsule_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE capsule_history SET deleted_at = now() WHERE capsule_history_id = ?")
@Where(clause = "is_deleted = false")
public class CapsuleHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "capsule_history_id", updatable = false)
    private Long capsuleHistoryId;

    @Column(name = "use_cnt", nullable = false)
    private Integer useCnt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Capsule capsule;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Builder
    public CapsuleHistory(Long capsuleHistoryId, Integer useCnt, Capsule capsule, LocalDateTime createdAt) {
        this.capsuleHistoryId = capsuleHistoryId;
        this.useCnt = useCnt;
        this.capsule = capsule;
        this.createdAt = createdAt;
    }
}
