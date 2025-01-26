package org.migros.domain.common.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourierNearStoreException extends RuntimeException {

    private final String key;
    private final String[] args;

    public CourierNearStoreException(String key) {
        super(key);
        this.key = key;
        args = new String[0];
    }

    public CourierNearStoreException(String key, String... args) {
        super(key);
        this.key = key;
        this.args = args;
    }
}