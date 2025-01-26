package org.migros.domain.adapter.courier.factory;

import org.migros.domain.adapter.courier.factory.enums.DistanceCalculationType;
import org.migros.domain.common.DomainComponent;

@DomainComponent
public class DistanceCalculatorFactory {

    public DistanceCalculator createDistanceCalculator(DistanceCalculationType type) {

        return switch (type) {
            case HAVERSINE -> new HaversineDistanceCalculator();
            default -> throw new IllegalArgumentException("Unsupported distance calculation type: " + type);
        };
    }
}
