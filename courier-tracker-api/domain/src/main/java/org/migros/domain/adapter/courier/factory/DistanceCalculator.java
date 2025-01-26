package org.migros.domain.adapter.courier.factory;

import org.migros.domain.adapter.courier.factory.enums.DistanceUnit;

public interface DistanceCalculator {
    double calculateDistance(double lat1, double lng1, double lat2, double lng2, DistanceUnit distanceUnit);
}
