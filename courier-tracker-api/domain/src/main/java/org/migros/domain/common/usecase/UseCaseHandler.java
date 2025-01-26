package org.migros.domain.common.usecase;

import org.migros.domain.common.DomainComponent;

@DomainComponent
public interface UseCaseHandler<E, T> {

    E handle(T useCase);

}
