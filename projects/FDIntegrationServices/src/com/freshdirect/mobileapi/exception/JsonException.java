package com.freshdirect.mobileapi.exception;

public class JsonException extends Exception {

    public JsonException() {
        super();
    }

    public JsonException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonException(String message) {
        super(message);
    }

    public JsonException(Throwable cause) {
        super(cause);
    }
}
