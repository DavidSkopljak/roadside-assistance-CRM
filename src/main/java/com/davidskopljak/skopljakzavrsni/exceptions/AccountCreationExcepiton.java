package com.davidskopljak.skopljakzavrsni.exceptions;

public class AccountCreationExcepiton extends RuntimeException {
    public AccountCreationExcepiton(String message) {
        super(message);
    }

    public AccountCreationExcepiton(Throwable cause) {
        super(cause);
    }
}
