package org.migros.adapter.courier;

import lombok.RequiredArgsConstructor;
import org.migros.adapter.courier.entity.CourierEntity;
import org.migros.adapter.courier.repository.CourierRepository;
import org.migros.domain.adapter.courier.model.CourierModel;
import org.migros.domain.adapter.courier.port.CourierPort;
import org.migros.domain.adapter.courier.usecase.CourierLogLocationUseCase;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourierDataAdapter implements CourierPort {

    private final CourierRepository repository;

    @Override
    public void logLocation(CourierLogLocationUseCase useCase, String storeName) {

        var entity = getCourierEntity(useCase, storeName);

        repository.save(entity);
    }

    @Override
    public boolean hasEntryWithinTimeRange(String courierId, String name, LocalDateTime time) {

        return repository.existsByCourierIdAndStoreNameAndEntranceTimeIsGreaterThanEqual(courierId, name, time);
    }

    @Override
    public List<CourierModel> getLocationByCourierId(String courierId) {

        var locations = repository.findAllByCourierIdOrderByEntranceTimeAsc(courierId);
        return  locations.stream().map(l -> new CourierModel(l.getLat(), l.getLng())).collect(Collectors.toList());
    }

    private CourierEntity getCourierEntity(CourierLogLocationUseCase useCase, String storeName) {

        var courierEntity = new CourierEntity();
        courierEntity.setCourierId(useCase.getCourierId());
        courierEntity.setLat(useCase.getLat());
        courierEntity.setLng(useCase.getLng());
        courierEntity.setStoreName(storeName);
        courierEntity.setEntranceTime(useCase.getTime());

        return courierEntity;
    }
}
