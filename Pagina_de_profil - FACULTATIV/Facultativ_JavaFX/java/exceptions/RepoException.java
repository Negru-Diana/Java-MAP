package com.example.facultativ_javafx.exceptions;

public class RepoException extends RuntimeException {
    public RepoException() {}

    public RepoException(String message) { super(message); }

    public RepoException(String message, Throwable cause) { super(message, cause); }

    public RepoException(Throwable cause) { super(cause); }

    public RepoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
