package dev.oguzhanercelik.model.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorEnum {

    UNEXPECTED_ERROR(1000, "unexpected.error", HttpStatus.INTERNAL_SERVER_ERROR),
    VALIDATION_ERROR(1001, "validation.error", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(1002, "auth.header.not.valid", HttpStatus.UNAUTHORIZED),
    USER_NOT_FOUND(1003, "user.not.found", HttpStatus.NOT_FOUND),
    EMAIL_ALREADY_EXIST(1004, "email.already.exist", HttpStatus.BAD_REQUEST),
    USER_NOT_CREATED(1005, "user.not.created", HttpStatus.INTERNAL_SERVER_ERROR),
    LIST_NOT_FOUND(1006, "list.not.found", HttpStatus.NOT_FOUND),
    TASK_NOT_FOUND(1007, "task.not.found", HttpStatus.NOT_FOUND);

    private final int code;
    private final String key;
    private final HttpStatus status;

}