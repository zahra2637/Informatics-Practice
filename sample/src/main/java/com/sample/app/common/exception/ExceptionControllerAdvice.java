package com.sample.app.common.exception;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.nio.file.AccessDeniedException;
import java.util.*;

@ControllerAdvice
public class ExceptionControllerAdvice {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public void handle(Exception ex) {
        LOGGER.error("unhandled exception occur", ex);
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void handle(UnsupportedOperationException ex) {
        LOGGER.error("not allowed service called", ex);
    }

    @ExceptionHandler(InvalidFieldValueException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ResponseBody
    public Errors handle(InvalidFieldValueException ex, Locale locale) {
        LOGGER.debug("invalid field value {}", ex.getErrors());
        return ex.getErrors();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public void handle(AccessDeniedException ex, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LOGGER.debug("access denied, user: [{}], url[{} {}] ", authentication.getName(), request.getMethod(), request.getRequestURI());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ResponseBody
    public String handle(HttpMessageNotReadableException ex) {
        LOGGER.debug("invalid http request data {}", ex.getMessage());
        if (ex.getCause() instanceof UnrecognizedPropertyException exception) {
            return "invalid property: " + exception.getPropertyName();
        } else {
            LOGGER.warn("HttpMessageNotReadable", ex);
            return null;
        }
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ResponseBody
    public String handle(HttpMediaTypeNotSupportedException ex, HttpServletRequest request) {
        LOGGER.warn("call [{}] with invalid content type [{}]", request.getRequestURI(), ex.getContentType());
        return ex.getMessage();
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ResponseBody
    public String handle(HttpMediaTypeNotAcceptableException ex, HttpServletRequest request) {
        LOGGER.warn("call [{}] with invalid accept header [{}]", request.getRequestURI(), ex.getMessage());
        return ex.getMessage();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ResponseBody
    public Errors handle(MethodArgumentNotValidException ex, Locale locale) {
        LOGGER.debug("method argument not valid, [{}]", ex.getMessage());
        Errors errors = new Errors();
        ex.getBindingResult().getGlobalErrors().stream().map(error -> {
            if (null != error.getArguments() && error.getArguments().length > 0 && null != error.getCode()) {
                return messageSource.getMessage(error.getCode(), error.getArguments(), error.getCode(), locale);
            } else {
                return error.getCode();
            }
        }).forEach(errors::addError);
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            if (null != error.getArguments() && error.getArguments().length > 0 && null != error.getCode()) {
                errors.addFieldError(error.getField(), messageSource.getMessage(error.getCode(), error.getArguments(), error.getCode(), locale));
            } else {
                errors.addFieldError(error.getField(), error.getCode());
            }
        });
        return errors;
    }

    public static class Errors {
        private final List<String> errors = new ArrayList<>();
        private final Map<String, List<String>> fieldErrors = new HashMap<>();

        public Errors(List<String> errors) {
            this.errors.addAll(errors);
        }

        public Errors(String... errors) {
            if (errors.length > 0) {
                this.errors.addAll(Arrays.asList(errors));
            }
        }

        public void addError(String error) {
            this.errors.add(error);
        }

        public void addFieldError(String field, String error) {
            this.fieldErrors.putIfAbsent(field, new ArrayList<>());
            this.fieldErrors.get(field).add(error);
        }

        public List<String> getErrors() {
            return errors;
        }

        public Map<String, List<String>> getFieldErrors() {
            return fieldErrors;
        }
    }
}
