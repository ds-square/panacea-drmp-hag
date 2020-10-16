package org.panacea.drmp.hag.exception;


import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HAGException extends RuntimeException {

    protected Throwable throwable;

    public HAGException(String message) {
        super(message);
    }

    public HAGException(String message, Throwable throwable) {
        super(message);
        this.throwable = throwable;
        log.error("HAGException: ", message);
    }

    public Throwable getCause() {
        return throwable;
    }
}

