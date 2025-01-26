package org.migros.domain.adapter.courier.port;

import org.migros.domain.adapter.courier.model.CourierModel;
import org.migros.domain.adapter.courier.usecase.CourierLogLocationUseCase;

import java.time.LocalDateTime;
import java.util.List;

public interface CourierPort {

    void logLocation(CourierLogLocationUseCase useCase, String storeName);

    boolean hasEntryWithinTimeRange(String courierId, String name, LocalDateTime time);

    List<CourierModel> getLocationByCourierId(String courierId);
}
