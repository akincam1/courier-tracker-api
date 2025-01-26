package org.migros.adapter.store.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import org.migros.adapter.store.entity.StoreEntity;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class StoreRepository {

    private static StoreRepository instance;

    @Getter
    private final Set<StoreEntity> storeEntities;

    private StoreRepository() {
        storeEntities = loadStoresFromJsonFile();
    }

    public static synchronized StoreRepository getInstance() {

        if (Objects.isNull(instance)) {
            instance = new StoreRepository();
        }
        return instance;
    }

    private Set<StoreEntity> loadStoresFromJsonFile() {

        var objMapper = new ObjectMapper();

        InputStream inputStream;
        try {
            inputStream = new ClassPathResource("stores.json").getInputStream();

            List<StoreEntity> stores = objMapper.readValue(inputStream, new TypeReference<>() {});
            return new HashSet<>(stores);
        } catch (IOException e) {
            throw new RuntimeException("Error loading stores from Json", e);
        }
    }
}
