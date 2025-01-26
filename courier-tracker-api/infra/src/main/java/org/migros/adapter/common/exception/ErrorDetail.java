package org.migros.adapter.common.exception;

public record ErrorDetail(String code, String message) {

    public static final String CODE_PREFIX = "COURIER-TRACKER-API-";

}
