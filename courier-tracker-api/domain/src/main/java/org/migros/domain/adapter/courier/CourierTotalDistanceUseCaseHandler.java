package org.migros.domain.adapter.courier;

import lombok.RequiredArgsConstructor;
import org.migros.domain.adapter.courier.factory.enums.DistanceUnit;
import org.migros.domain.adapter.courier.model.CourierModel;
import org.migros.domain.adapter.courier.port.CourierPort;
import org.migros.domain.adapter.courier.factory.DistanceCalculatorFactory;
import org.migros.domain.adapter.courier.usecase.CourierTotalDistanceUseCase;
import org.migros.domain.common.DomainComponent;
import org.migros.domain.common.usecase.UseCaseHandler;

import java.util.List;

import static org.migros.domain.adapter.courier.factory.enums.DistanceCalculationType.HAVERSINE;

@DomainComponent
@RequiredArgsConstructor
public class CourierTotalDistanceUseCaseHandler implements UseCaseHandler<Double, CourierTotalDistanceUseCase> {

    private final CourierPort port;

    private final DistanceCalculatorFactory distanceCalculatorFactory;

    @Override
    public Double handle(CourierTotalDistanceUseCase useCase) {

        var locations = port.getLocationByCourierId(useCase.getCourierId());

        return getTotalTravelDistance(locations, useCase.getDistanceUnit());
    }

    public double getTotalTravelDistance(List<CourierModel> locations, DistanceUnit distanceUnit) {

        if (locations == null || locations.size() < 2) {
            return 0.0;
        }

        var calculator = distanceCalculatorFactory.createDistanceCalculator(HAVERSINE);

        double totalDistance = 0.0;
        for (int i = 0; i < locations.size() - 1; i++) {
            CourierModel prev = locations.get(i);
            CourierModel current = locations.get(i+1);
            totalDistance += calculator.calculateDistance(prev.lat(), current.lat(), prev.lng(), current.lng(), distanceUnit);
        }

        return totalDistance;
    }
}
