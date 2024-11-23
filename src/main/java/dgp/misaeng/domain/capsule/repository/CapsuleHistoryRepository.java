package dgp.misaeng.domain.capsule.repository;

import dgp.misaeng.domain.capsule.entity.Capsule;
import dgp.misaeng.domain.capsule.entity.CapsuleHistory;
import dgp.misaeng.global.util.enums.CapsuleType;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CapsuleHistoryRepository extends JpaRepository<CapsuleHistory, Long> {
    @Query("SELECT c " +
            "FROM Capsule c " +
            "JOIN c.microbe m " +
            "JOIN m.device d " +
            "WHERE d.serialNum = :serialNum " +
            "AND c.capsuleType = :capsuleType " +
            "AND m.isDeleted = false " +
            "AND d.isDeleted = false " +
            "AND m.survive = true")
    Optional<Capsule> findBySerialNumAndCapsuleType(
            @Param("serialNum") String serialNum,
            @Param("capsuleType") CapsuleType capsuleType);
}
