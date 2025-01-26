package org.migros.domain.adapter.store;

import lombok.RequiredArgsConstructor;
import org.migros.domain.adapter.store.model.StoreModel;
import org.migros.domain.adapter.store.port.StorePort;
import org.migros.domain.common.DomainComponent;
import org.migros.domain.common.exception.StoreListEmptyException;
import org.migros.domain.common.usecase.VoidResponseUseCaseHandler;

import java.util.List;
import java.util.Set;

@DomainComponent
@RequiredArgsConstructor
public class StoreGetAllResponseUseCaseHandler implements VoidResponseUseCaseHandler<Set<StoreModel>> {

    private final StorePort storePort;

    @Override
    public Set<StoreModel> handle() {

        var stores = storePort.getAllStore();

        if (stores == null || stores.isEmpty()) {
            throw new StoreListEmptyException();
        }

        return stores;
    }
}
