package org.migros.domain.adapter.courier.usecase;

import lombok.*;
import org.migros.domain.common.usecase.UseCase;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CourierLogLocationUseCase implements UseCase {

    private String courierId;

    private Double lat;

    private Double lng;

    private LocalDateTime time;
}