package dgp.misaeng.domain.capsule.entity;

import dgp.misaeng.domain.microbe.entity.Microbe;
import dgp.misaeng.global.util.enums.CapsuleType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "capsule")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE capsule SET deleted_at = now() WHERE capsule_id = ?")
@Where(clause = "is_deleted = false")
public class Capsule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long capsuleId;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Enumerated(EnumType.STRING)
    @Column(name = "capsule_type", nullable = false)
    private CapsuleType capsuleType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "microbe_id", nullable = false)
    private Microbe microbe;

    @Builder
    public Capsule(Long capsuleId, Integer stock, CapsuleType capsuleType, Microbe microbe) {
        this.capsuleId = capsuleId;
        this.stock = stock;
        this.capsuleType = capsuleType;
        this.microbe = microbe;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
