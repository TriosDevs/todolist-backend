package dev.oguzhanercelik.exception;

public class TokenGenerateException extends RuntimeException {

    public TokenGenerateException(String message) {
        super("JWT Token could not generated, " + message);
    }
}