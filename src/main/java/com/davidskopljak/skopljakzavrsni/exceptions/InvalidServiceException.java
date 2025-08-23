package com.davidskopljak.skopljakzavrsni.exceptions;

public class InvalidServiceException extends RuntimeException {
    public InvalidServiceException(String message) {
        super(message);
    }
}
