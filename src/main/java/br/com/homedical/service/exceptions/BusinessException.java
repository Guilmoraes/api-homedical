package br.com.homedical.service.exceptions;

import org.springframework.http.HttpStatus;

import java.util.function.Supplier;

public class BusinessException extends RuntimeException {

    private final int code;

    private String description;

    private HttpStatus status = HttpStatus.BAD_REQUEST;

    public static Supplier<BusinessException> of(ErrorConstants error) {
        return () -> new BusinessException(error);
    }

    public static Supplier<BusinessException> of(ErrorConstants error, HttpStatus status) {
        return () -> new BusinessException(error, status);
    }

    public BusinessException(ErrorConstants error) {
        super(error.getValue());
        this.code = error.ordinal();
        this.description = error.getMessage();
    }

    public BusinessException(String message) {
        super(message);
        this.code = 0;
    }

    public BusinessException(String message, int code) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorConstants error, HttpStatus status) {
        super(error.getValue());
        this.code = error.ordinal();
        this.description = error.getMessage();
        this.status = status;
    }

    public BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
        code = 0;
    }

    public BusinessException(String message, String description) {
        super(message);
        this.code = 0;
        this.description = description;
    }

    public BusinessException(String message, String description, HttpStatus status) {
        super(message);
        this.code = 0;
        this.status = status;
        this.description = description;
    }

    public BusinessException(String message, String description, int code, HttpStatus status) {
        super(message);
        this.code = code;
        this.status = status;
        this.description = description;
    }

    public BusinessException(String message, String description, int code) {
        super(message);
        this.code = code;
        this.description = description;
    }

    public BusinessException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    protected BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int code) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
    }

    public BusinessException(int code, HttpStatus status) {
        super();
        this.code = code;
        this.status = status;
    }

    public BusinessException(String message, int code, HttpStatus status) {
        super(message);
        this.code = code;
        this.status = status;
    }

    public BusinessException(String message, Throwable cause, int code, HttpStatus status) {
        super(message, cause);
        this.code = code;
        this.status = status;
    }

    public BusinessException(Throwable cause, int code, HttpStatus status) {
        super(cause);
        this.code = code;
        this.status = status;
    }

    protected BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, int code, HttpStatus status) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.code = code;
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }
}

