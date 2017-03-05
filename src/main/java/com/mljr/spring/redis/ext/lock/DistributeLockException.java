package com.mljr.spring.redis.ext.lock;

/**
 * Created by junqing.li on 17/3/4.
 */
public class DistributeLockException extends RuntimeException {

    public DistributeLockException() {
		super();
    }

    public DistributeLockException(String message) {
        super(message);
    }

    public DistributeLockException(String message, Throwable cause) {
        super(message, cause);
    }

    public DistributeLockException(Throwable cause) {
        super(cause);
    }

    protected DistributeLockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
