package org.migros.domain.adapter.courier.factory;

import org.migros.domain.adapter.courier.factory.enums.DistanceUnit;

import static org.migros.domain.adapter.courier.factory.enums.DistanceUnit.*;

public class HaversineDistanceCalculator implements DistanceCalculator {

    private static final int RADIUS = 6371;

    @Override
    public double calculateDistance(double lat1, double lat2, double lng1, double lng2, DistanceUnit distanceUnit) {

        double latDistance = Math.toRadians(lat2 - lat1);
        double lngDistance = Math.toRadians(lng1 - lng2);

        double haversineValue = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double centralAngle = 2 * Math.atan2(Math.sqrt(haversineValue), Math.sqrt(1 - haversineValue));
        return RADIUS * centralAngle * getRadius(distanceUnit);
    }

    private double getRadius(DistanceUnit distanceUnit) {
        switch (distanceUnit) {
            case KM:
                return KM.getConversionFactor();
            case M:
                return M.getConversionFactor();
            case MILE:
                return MILE.getConversionFactor();
            default:
                throw new IllegalArgumentException("Invalid distance unit: " + distanceUnit);
        }
    }
}
