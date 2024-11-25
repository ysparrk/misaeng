package dgp.misaeng.domain.device.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import dgp.misaeng.domain.device.entity.Device;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface DeviceRepository extends JpaRepository<Device, Long>{
    @Query("SELECT d FROM Device d WHERE d.member.id = :memberId AND d.isDeleted = false")
    List<Device> findAllByMemberId(@Param("memberId") Long memberId);
}
