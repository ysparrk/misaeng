package dgp.misaeng.domain.microbe.repository;

import dgp.misaeng.domain.microbe.entity.Microbe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MicrobeRepository extends JpaRepository<Microbe, Long> {
}
