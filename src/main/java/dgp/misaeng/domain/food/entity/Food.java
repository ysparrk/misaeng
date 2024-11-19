package dgp.misaeng.domain.food.entity;

import dgp.misaeng.domain.device.entity.Device;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(name = "food")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE food SET deleted_at = now() WHERE food_id = ?")
@Where(clause = "is_deleted = false")
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_id", updatable = false)
    private Long foodId;

    @Column(name = "food_category", columnDefinition = "JSON")
    private String foodCategory;

    @Column(name = "rgb_stat", length = 11, nullable = false)
    private String rgbStat;

    @Column(name = "weight")
    private float weight;

    @Column(name = "imgUrl", nullable = false)
    private String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "modified_at", nullable = false)
    private LocalDateTime modifiedAt;

    @Builder
    public Food(Long foodId, String foodCategory, String rgbStat, float weight, String imgUrl, Device device, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.foodId = foodId;
        this.foodCategory = foodCategory;
        this.rgbStat = rgbStat;
        this.weight = weight;
        this.imgUrl = imgUrl;
        this.device = device;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
