package dev.oguzhanercelik.controller.advice;

import dev.oguzhanercelik.exception.ApiException;
import dev.oguzhanercelik.model.error.ErrorDetail;
import dev.oguzhanercelik.model.error.ErrorEnum;
import dev.oguzhanercelik.model.error.ErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static dev.oguzhanercelik.config.MdcConstant.ACCEPT_LANGUAGE;
import static dev.oguzhanercelik.config.MdcConstant.X_TRACE_ID;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    private final MessageSource messageSource;
    private final HttpServletRequest httpServletRequest;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handle(MethodArgumentNotValidException exception) {
        final String traceId = getTraceId();
        log.error("traceId: {} MethodArgumentNotValidException occurred. {}", traceId, exception);

        final List<ErrorDetail> errorDetails = getErrorDetailsByFieldErrors(exception.getBindingResult().getFieldErrors());
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .traceId(traceId)
                .exception("MethodArgumentNotValidException")
                .errors(errorDetails)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handle(BindException exception) {
        final String traceId = getTraceId();
        log.error("traceId: {} BindException occurred. {}", traceId, exception);

        final List<ErrorDetail> errorDetails = getErrorDetailsByFieldErrors(exception.getBindingResult().getFieldErrors());
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .traceId(traceId)
                .exception("BindException")
                .errors(errorDetails)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handle(ApiException exception) {
        final String traceId = getTraceId();
        log.error("traceId: {} MerchantApiException occurred. {}", traceId, exception);
        final String messageKey = exception.getError().getKey();
        final ErrorDetail errorDetail = ErrorDetail.builder()
                .code(exception.getError().getCode())
                .message(messageSource.getMessage(messageKey, exception.getArgs(), messageKey, getLocale()))
                .build();
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .traceId(traceId)
                .exception(exception.getClass().getSimpleName())
                .error(errorDetail)
                .build();
        return new ResponseEntity<>(errorResponse, exception.getError().getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handle(Exception exception) {
        final String traceId = getTraceId();
        log.error("traceId: {} Exception occurred. {}", traceId, exception);

        final ErrorEnum unexpectedError = ErrorEnum.UNEXPECTED_ERROR;
        final ErrorDetail errorDetail = ErrorDetail.builder()
                .code(unexpectedError.getCode())
                .message(messageSource.getMessage(unexpectedError.getKey(), null, unexpectedError.getKey(), getLocale()))
                .build();
        final ErrorResponse errorResponse = ErrorResponse.builder()
                .traceId(traceId)
                .exception("Exception")
                .error(errorDetail)
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private List<ErrorDetail> getErrorDetailsByFieldErrors(List<FieldError> fieldErrors) {
        final List<ErrorDetail> errorDetails = new ArrayList<>();
        fieldErrors.forEach(fieldError -> {
            final ErrorDetail errorDetail = new ErrorDetail(ErrorEnum.VALIDATION_ERROR.getCode(), getMessage(fieldError));
            errorDetails.add(errorDetail);
        });
        return errorDetails;
    }

    private String getMessage(FieldError error) {
        final String messageKey = error.getDefaultMessage();
        return messageSource.getMessage(Objects.requireNonNull(messageKey), error.getArguments(), messageKey, getLocale());
    }

    private Locale getLocale() {
        final String acceptLanguage = httpServletRequest.getHeader(ACCEPT_LANGUAGE);
        return new Locale(acceptLanguage);
    }

    private String getTraceId() {
        return httpServletRequest.getHeader(X_TRACE_ID);
    }
}