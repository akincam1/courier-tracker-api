package org.migros.domain.adapter.courier.factory.enums;

import lombok.Getter;

@Getter
public enum DistanceUnit {
    M(1000),
    KM(1),
    MILE(0.5);

    private final double conversionFactor;

    DistanceUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public static DistanceUnit fromString(String distanceUnit) {
        try {
            return DistanceUnit.valueOf(distanceUnit.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid strategy: " + distanceUnit);
        }
    }
}
