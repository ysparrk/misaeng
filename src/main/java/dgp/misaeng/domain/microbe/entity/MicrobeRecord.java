package dgp.misaeng.domain.microbe.entity;

import dgp.misaeng.domain.device.entity.Device;
import dgp.misaeng.global.util.enums.MicrobeSoilCondition;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(name = "microbe_record")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE microbe_record SET deleted_at = now() WHERE microbe_record_id = ?")
@Where(clause = "is_deleted = false")
public class MicrobeRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "microbe_record_id", updatable = false)
    private Long microbeRecordId;

    @Column(name = "food_category", columnDefinition = "JSON")
    private String foodCategory;

    @Column(name = "rgb_stat", length = 11, nullable = false)
    private String rgbStat;

    @Column(name = "weight")
    private float weight;

    @Enumerated(EnumType.STRING)
    @Column(name = "microbe_soil_ondition")
    private MicrobeSoilCondition microbeSoilCondition;

    @Column(name = "img_url", nullable = false)
    private String imgUrl;

    @Column(name = "is_empty", nullable = false)
    private boolean isEmpty;

    @ManyToOne(fetch = FetchType.LAZY)
    private Microbe microbe;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedAt;

}
