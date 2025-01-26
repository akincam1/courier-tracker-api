package org.migros.domain.adapter.courier.usecase;

import lombok.*;
import org.migros.domain.adapter.courier.factory.enums.DistanceUnit;
import org.migros.domain.common.usecase.UseCase;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CourierTotalDistanceUseCase implements UseCase {

    private String courierId;

    private DistanceUnit distanceUnit;
}
