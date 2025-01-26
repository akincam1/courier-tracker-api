package org.migros.domain.common.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StoreListEmptyException extends RuntimeException {

    public StoreListEmptyException() {
        super("Store List Empty !!");

    }
}