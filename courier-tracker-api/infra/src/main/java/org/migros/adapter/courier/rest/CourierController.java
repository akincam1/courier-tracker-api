package org.migros.adapter.courier.rest;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.migros.adapter.courier.model.request.CourierLogLocationRequest;
import org.migros.domain.adapter.courier.factory.enums.DistanceUnit;
import org.migros.domain.adapter.courier.usecase.CourierLogLocationUseCase;
import org.migros.domain.adapter.courier.usecase.CourierTotalDistanceUseCase;
import org.migros.domain.common.usecase.UseCaseHandler;
import org.migros.domain.common.usecase.VoidUseCaseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/courier")
public class CourierController {

    private final VoidUseCaseHandler<CourierLogLocationUseCase> courierLogLocationVoidUseCaseHandler;

    private final UseCaseHandler<Double, CourierTotalDistanceUseCase> courierTotalDistanceUseCaseHandler;

    @PostMapping(path = "/log-instant-location")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<Void> logInstantLocation(@RequestBody @Valid CourierLogLocationRequest request) {

        var useCase = getCourierLogLocationUseCase(request);
        courierLogLocationVoidUseCaseHandler.handle(useCase);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/{courierId}/totalDistance")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Double> getTotalTravelDistance(@PathVariable @NotBlank String courierId, @RequestParam(defaultValue = "km") String distanceUnit) {

        DistanceUnit unit = DistanceUnit.fromString(distanceUnit);

        var useCase = getCourierTotalDistanceUseCase(courierId, unit);
        var distance = courierTotalDistanceUseCaseHandler.handle(useCase);
        return ResponseEntity.ok(distance);
    }

    private CourierLogLocationUseCase getCourierLogLocationUseCase(CourierLogLocationRequest request) {
        return CourierLogLocationUseCase
                .builder()
                .courierId(request.courierId())
                .lat(request.lat())
                .lng(request.lng())
                .time(request.time())
                .build();
    }

    private CourierTotalDistanceUseCase getCourierTotalDistanceUseCase(String courierId, DistanceUnit distanceUnit) {
        return CourierTotalDistanceUseCase.builder().courierId(courierId).distanceUnit(distanceUnit).build();
    }

}
