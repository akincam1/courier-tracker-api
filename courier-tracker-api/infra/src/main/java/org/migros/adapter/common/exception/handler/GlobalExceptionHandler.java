package org.migros.adapter.common.exception.handler;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.migros.adapter.common.exception.ErrorDetail;
import org.migros.adapter.common.exception.ErrorEnum;
import org.migros.adapter.common.exception.ErrorResponseModel;
import org.migros.domain.common.exception.CourierNearStoreException;
import org.migros.domain.common.exception.StoreListEmptyException;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseModel> handleGenericException(Exception exception) {
        String traceId = generateTraceId();
        log.error("TraceID: {} Exception occurred: {}", traceId, exception);

        String message = StringUtils.isNotBlank(exception.getMessage()) ? exception.getMessage() : "An unexpected error has occurred.";
        ErrorResponseModel response = buildErrorResponse(ErrorEnum.UNEXPECTED_ERROR, message, traceId, exception);

        return ResponseEntity.status(ErrorEnum.UNEXPECTED_ERROR.getStatus()).body(response);
    }

    @ExceptionHandler(CourierNearStoreException.class)
    public ResponseEntity<ErrorResponseModel> handleCourierNearStoreException(CourierNearStoreException exception) {
        String traceId = generateTraceId();
        log.error("TraceID: {} CourierNearStoreException occurred: {}", traceId, exception);

        String message = StringUtils.isNotBlank(exception.getKey()) ? exception.getKey() : "A conflict error occurred.";
        ErrorResponseModel response = buildErrorResponse(ErrorEnum.CONFLICT_ERROR, message, traceId, exception);

        return ResponseEntity.status(ErrorEnum.CONFLICT_ERROR.getStatus()).body(response);
    }

    @ExceptionHandler(StoreListEmptyException.class)
    public ResponseEntity<ErrorResponseModel> handleStoreListEmptyException(StoreListEmptyException exception) {
        String traceId = generateTraceId();
        log.error("TraceID: {} StoreListEmptyException occurred: {}", traceId, exception);

        ErrorResponseModel response = buildErrorResponse(ErrorEnum.NO_CONTENT_ERROR, "", traceId, exception);

        return ResponseEntity.status(ErrorEnum.NO_CONTENT_ERROR.getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseModel> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String traceId = generateTraceId();
        log.error("TraceID: {} MethodArgumentNotValidException occurred: {}", traceId, exception);

        List<String> errorMessages = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::formatErrorMessage)
                .collect(Collectors.toList());

        String message = String.join(", ", errorMessages);
        ErrorResponseModel response = buildErrorResponse(ErrorEnum.BAD_REQUEST_ERROR, message, traceId, exception);

        return ResponseEntity.status(ErrorEnum.BAD_REQUEST_ERROR.getStatus()).body(response);
    }

    private String formatErrorMessage(FieldError fieldError) {
        String field = fieldError.getField();
        String defaultMessage = fieldError.getDefaultMessage();
        return field + ": " + defaultMessage;
    }

    private String generateTraceId() {
        return UUID.randomUUID().toString();
    }

    private ErrorResponseModel buildErrorResponse(ErrorEnum errorEnum, String message, String traceId, Exception exception) {
        ErrorDetail detail = new ErrorDetail(errorEnum.getCode(), message);
        return new ErrorResponseModel(traceId, detail, exception.getClass().getSimpleName(), LocalDateTime.now());
    }
}
