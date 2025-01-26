package org.migros.adapter.store.rest;

import lombok.RequiredArgsConstructor;
import org.migros.domain.adapter.store.model.StoreModel;
import org.migros.domain.common.usecase.VoidResponseUseCaseHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/store")
public class StoreController {

    private final VoidResponseUseCaseHandler<Set<StoreModel>> storeGetAllVoidUseCaseHandler;

    @GetMapping(path = "/all")
    @ResponseStatus(code = HttpStatus.OK)
    public ResponseEntity<Set<StoreModel>> getAllStore() {

        var response = storeGetAllVoidUseCaseHandler.handle();
        return ResponseEntity.ok(response);
    }
}
