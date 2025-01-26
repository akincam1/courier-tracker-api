package org.migros.domain.common.usecase;

import org.migros.domain.common.DomainComponent;

@DomainComponent
public interface VoidUseCaseHandler<T extends UseCase> {

    void handle(T useCase);

}
