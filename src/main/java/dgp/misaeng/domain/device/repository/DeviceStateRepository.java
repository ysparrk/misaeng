package dgp.misaeng.domain.device.repository;

import dgp.misaeng.domain.device.entity.DeviceState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DeviceStateRepository extends JpaRepository<DeviceState, Long> {
    @Query("SELECT ds FROM DeviceState ds " +
            "WHERE ds.device.deviceId = :deviceId " +
            "AND ds.device.isDeleted = false")
    Optional<DeviceState> findByDeviceId(@Param("deviceId") Long deviceId);

    @Query("SELECT ds FROM DeviceState ds " +
            "WHERE ds.device.serialNum = :serialNum " +
            "AND ds.device.isDeleted = false")
    Optional<DeviceState> findBySerialNum(@Param("serialNum") String serialNum);
}
