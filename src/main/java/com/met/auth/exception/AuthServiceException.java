package com.met.auth.exception;

public class AuthServiceException extends RuntimeException {

    private ErrorCode errorCode;

    public AuthServiceException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}
