package net.omar.framework.ioc.exceptions;

public class NoSuchBeanException extends RuntimeException {
    
    public NoSuchBeanException(String message) {
        super(message);
    }
    
    public NoSuchBeanException(String message, Throwable cause) {
        super(message, cause);
    }
}