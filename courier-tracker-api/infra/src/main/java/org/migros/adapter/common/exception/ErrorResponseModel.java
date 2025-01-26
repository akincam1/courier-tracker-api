package org.migros.adapter.common.exception;

import java.time.LocalDateTime;


public record ErrorResponseModel(String traceId, ErrorDetail detail, String exception, LocalDateTime occurredAt) {

}
