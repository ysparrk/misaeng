package dgp.misaeng.domain.capsule.repository;

import dgp.misaeng.domain.capsule.entity.Capsule;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CapsuleRepository extends JpaRepository<Capsule, Long> {
    @Query("SELECT c " +
            "FROM Capsule c " +
            "JOIN c.microbe m " +
            "JOIN m.device d " +
            "WHERE d.serialNum = :serialNum " +
            "AND m.isDeleted = false " +
            "AND d.isDeleted = false " +
            "AND m.survive = true")
    List<Capsule> findAllBySerialNum(@Param("serialNum") String serialNum);
}
