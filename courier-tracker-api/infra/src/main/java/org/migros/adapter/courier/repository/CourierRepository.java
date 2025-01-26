package org.migros.adapter.courier.repository;

import org.migros.adapter.courier.entity.CourierEntity;
import org.migros.adapter.courier.entity.CourierProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CourierRepository extends JpaRepository<CourierEntity, Long> {

    boolean existsByCourierIdAndStoreNameAndEntranceTimeIsGreaterThanEqual(String courierId, String storeName, LocalDateTime time);

    List<CourierProjection> findAllByCourierIdOrderByEntranceTimeAsc(String courierId);
}
