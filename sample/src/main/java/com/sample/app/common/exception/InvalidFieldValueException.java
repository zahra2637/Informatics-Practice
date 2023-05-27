package com.sample.app.common.exception;


import java.util.Arrays;

public class InvalidFieldValueException extends RuntimeException {
    private final ExceptionControllerAdvice.Errors errors = new ExceptionControllerAdvice.Errors();

    public InvalidFieldValueException(String... errorCodes) {
        Arrays.stream(errorCodes).forEach(this.errors::addError);
    }

    public InvalidFieldValueException addGlobalError(String errorCode) {
        this.errors.addError(errorCode);
        return this;
    }

    public InvalidFieldValueException addFieldError(String field, String errorCode) {
        this.errors.addFieldError(field, errorCode);
        return this;
    }

    public ExceptionControllerAdvice.Errors getErrors() {
        return errors;
    }
}
