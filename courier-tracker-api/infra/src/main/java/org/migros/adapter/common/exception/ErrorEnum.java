package org.migros.adapter.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorEnum {
    UNEXPECTED_ERROR(ErrorDetail.CODE_PREFIX.concat("5000"), "unexpected.error", HttpStatus.INTERNAL_SERVER_ERROR),

    CONFLICT_ERROR(ErrorDetail.CODE_PREFIX.concat("4009"), "conflict.error", HttpStatus.CONFLICT),

    NO_CONTENT_ERROR(ErrorDetail.CODE_PREFIX.concat("2004"), "no_content.error", HttpStatus.NO_CONTENT),

    BAD_REQUEST_ERROR(ErrorDetail.CODE_PREFIX.concat("4000"), "bad_request.error", HttpStatus.BAD_REQUEST);

    private final String code;

    private final String messageKey;

    private final HttpStatus status;
}
