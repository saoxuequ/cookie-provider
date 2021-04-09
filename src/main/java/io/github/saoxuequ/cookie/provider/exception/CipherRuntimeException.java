package io.github.saoxuequ.cookie.provider.exception;

/**
 * Created by han on 18/8/3.
 */
public class CipherRuntimeException extends RuntimeException {
    public CipherRuntimeException() {
    }

    public CipherRuntimeException(String message) {
        super(message);
    }

    public CipherRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public CipherRuntimeException(Throwable cause) {
        super(cause);
    }

    public CipherRuntimeException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace
    ) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
