package dgp.misaeng.domain.environment.repository;

import dgp.misaeng.domain.environment.entity.Environment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnvironmentRepository extends JpaRepository<Environment, Long> {
}
