package org.migros.adapter.store;

import org.migros.adapter.store.repository.StoreRepository;
import org.migros.domain.adapter.store.model.StoreModel;
import org.migros.domain.adapter.store.port.StorePort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StoreDataAdapter implements StorePort {

    private final StoreRepository repository;

    public StoreDataAdapter() {
        repository = StoreRepository.getInstance();
    }

    @Override
    public Set<StoreModel> getAllStore() {

        var storeEntities = repository.getStoreEntities();

        var storeModels  = new HashSet<StoreModel>();
        if (Objects.isNull(storeEntities) || storeEntities.isEmpty()) {
            return storeModels;
        }

        storeEntities.forEach(entity -> storeModels.add(new StoreModel(entity.name(), entity.lat(), entity.lng())));
        return storeModels;
    }
}
