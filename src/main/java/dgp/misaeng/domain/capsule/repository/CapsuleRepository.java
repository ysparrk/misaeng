package dgp.misaeng.domain.capsule.repository;

import dgp.misaeng.domain.capsule.entity.Capsule;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CapsuleRepository extends JpaRepository<Capsule, Long> {
    @Query("SELECT c " +
            "FROM Capsule c " +
            "JOIN c.microbe m " +
            "JOIN m.device d " +
            "WHERE d.serialNum = :serialNum " +
            "AND m.isDeleted = false " +
            "AND m.survive = true " +
            "AND d.isDeleted = false")
    List<Capsule> findAllBySerialNum(@Param("serialNum") String serialNum);

    @Query("SELECT c FROM Capsule c " +
            "WHERE c.microbe.microbeId = :microbeId " +
            "AND c.microbe.isDeleted = false " +
            "AND c.microbe.survive = true")
    List<Capsule> findAllByMicrobeId(@Param("microbeId") Long microbeId);

}
