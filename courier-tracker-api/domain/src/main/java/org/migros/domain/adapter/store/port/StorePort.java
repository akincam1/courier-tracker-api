package org.migros.domain.adapter.store.port;

import org.migros.domain.adapter.store.model.StoreModel;

import java.util.List;
import java.util.Set;

public interface StorePort {

    Set<StoreModel> getAllStore();
}
