package dgp.misaeng.domain.member.entity;

import dgp.misaeng.global.util.enums.AuthProvider;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE member SET deleted_at = now() WHERE id = ?")
@Where(clause = "is_deleted = false")
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "email", length = 50, nullable = false)
    private String email;

    @Column(name = "name", length = 12, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider")
    AuthProvider authProvider;

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
    public Member(Long id, String email, String name, LocalDateTime createdAt, LocalDateTime modifiedAt, Boolean isDeleted, LocalDateTime deletedAt) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
        this.isDeleted = isDeleted;
        this.deletedAt = deletedAt;
    }
}
