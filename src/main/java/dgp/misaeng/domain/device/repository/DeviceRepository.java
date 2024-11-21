package dgp.misaeng.domain.device.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dgp.misaeng.domain.device.entity.Device;


public interface DeviceRepository extends JpaRepository<Device, Long>{

}
