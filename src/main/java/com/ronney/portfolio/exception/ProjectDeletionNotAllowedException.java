package com.ronney.portfolio.exception;

public class ProjectDeletionNotAllowedException extends RuntimeException {
    public ProjectDeletionNotAllowedException(String message) {
        super(message);
    }
}
