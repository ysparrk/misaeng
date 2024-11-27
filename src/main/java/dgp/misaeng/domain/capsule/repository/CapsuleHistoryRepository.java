package dgp.misaeng.domain.capsule.repository;

import dgp.misaeng.domain.capsule.entity.CapsuleHistory;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CapsuleHistoryRepository extends JpaRepository<CapsuleHistory, Long> {
    @Query("SELECT ch FROM CapsuleHistory ch " +
            "JOIN ch.capsule c " +
            "WHERE c.microbe.microbeId = :microbeId " +
            "AND ch.useState = true " +
            "ORDER BY ch.createdAt DESC")
    List<CapsuleHistory> findRecentThreeByMicrobeId(@Param("microbeId") Long microbeId, Pageable pageable);
}
