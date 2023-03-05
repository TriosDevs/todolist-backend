package dev.oguzhanercelik.exception;

import dev.oguzhanercelik.model.error.ErrorEnum;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ApiException extends RuntimeException {

    private final ErrorEnum error;
    private final String[] args;

    public ApiException(ErrorEnum error) {
        this.error = error;
        this.args = new String[0];
    }

    public ApiException(ErrorEnum error, String... args) {
        this.error = error;
        this.args = args;
    }
}