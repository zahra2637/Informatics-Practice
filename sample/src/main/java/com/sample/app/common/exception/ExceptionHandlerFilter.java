package com.sample.app.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;

import java.io.IOException;
import java.lang.reflect.Method;

public class ExceptionHandlerFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerFilter.class);

    private final ObjectMapper objectMapper;
    private final ExceptionControllerAdvice exceptionControllerAdvice;
    private final ExceptionHandlerMethodResolver exceptionHandlerMethodResolver;

    public ExceptionHandlerFilter(ExceptionControllerAdvice exceptionControllerAdvice, ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.exceptionControllerAdvice = exceptionControllerAdvice;
        this.exceptionHandlerMethodResolver = new ExceptionHandlerMethodResolver(exceptionControllerAdvice.getClass());
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    public void handle(HttpServletResponse response, Throwable ex) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        Method method = exceptionHandlerMethodResolver.resolveMethodByExceptionType(ex.getClass());
        if (null == method) {
            LOGGER.error("{} exception occurred, but can't handle it using advices", ex.getClass(), ex);
            return;
        }
        try {
            Object resolverResponse = method.invoke(exceptionControllerAdvice, ex);
            objectMapper.writeValue(response.getWriter(), resolverResponse);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        } catch (Exception e) {
            LOGGER.error("error calling exception\n \nhandler method [{}] and setting result to response", method.getName(), e);
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Throwable ex) {
            if (ex instanceof ServletException && null != ex.getCause()) {
                this.handle((HttpServletResponse) servletRequest, ex.getCause());
            } else {
                this.handle((HttpServletResponse) servletResponse, ex);
            }
        }
    }
}
