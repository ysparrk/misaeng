package dgp.misaeng.domain.microbe.repository;

import dgp.misaeng.domain.microbe.entity.Microbe;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MicrobeRepository extends JpaRepository<Microbe, Long> {
    @Query("SELECT m.microbeId FROM Microbe m " +
            "JOIN m.device d " +
            "WHERE d.serialNum = :serialNum " +
            "AND m.isDeleted = false " +
            "AND d.isDeleted = false " +
            "AND m.survive = true")
    Optional<Long> findMicrobeIdBySerialNum(@Param("serialNum") String serialNum);
}
