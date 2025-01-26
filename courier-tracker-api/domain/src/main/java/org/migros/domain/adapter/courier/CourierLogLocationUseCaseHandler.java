package org.migros.domain.adapter.courier;

import lombok.RequiredArgsConstructor;
import org.migros.domain.adapter.courier.factory.enums.DistanceUnit;
import org.migros.domain.adapter.courier.port.CourierPort;
import org.migros.domain.adapter.courier.factory.DistanceCalculatorFactory;
import org.migros.domain.adapter.courier.usecase.CourierLogLocationUseCase;
import org.migros.domain.adapter.store.model.StoreModel;
import org.migros.domain.adapter.store.port.StorePort;
import org.migros.domain.common.DomainComponent;
import org.migros.domain.common.exception.CourierNearStoreException;
import org.migros.domain.common.usecase.VoidUseCaseHandler;

import java.time.LocalDateTime;

import static org.migros.domain.adapter.courier.factory.enums.DistanceCalculationType.HAVERSINE;

@DomainComponent
@RequiredArgsConstructor
public class CourierLogLocationUseCaseHandler implements VoidUseCaseHandler<CourierLogLocationUseCase> {

    private static final double DISTANCE_COMPARATOR = 100;

    private final CourierPort courierPort;

    private final StorePort storePort;

    private final DistanceCalculatorFactory distanceCalculatorFactory;

    @Override
    public void handle(CourierLogLocationUseCase useCase) {

        var stores = storePort.getAllStore();

        var calculator = distanceCalculatorFactory.createDistanceCalculator(HAVERSINE);

        var isValid = false;

        for (StoreModel storeModel : stores) {
            var distance = calculator.calculateDistance(useCase.getLat(), storeModel.lat(), useCase.getLng(), storeModel.lng(), DistanceUnit.M);
            if (distance < DISTANCE_COMPARATOR) {
                LocalDateTime minusMinutes = useCase.getTime().minusMinutes(1);
                var exist = courierPort.hasEntryWithinTimeRange(useCase.getCourierId(), storeModel.name(), minusMinutes);

                if (!exist) {
                    courierPort.logLocation(useCase, storeModel.name());
                    isValid = true;
                    break;
                }
            }
        }

        if (!isValid) {
            throw new CourierNearStoreException(String.format("Courier cannot operate in the specified area (lat: %.6f, lng: %.6f) within the last minute.", useCase.getLat(), useCase.getLng()));
        }
    }
}
