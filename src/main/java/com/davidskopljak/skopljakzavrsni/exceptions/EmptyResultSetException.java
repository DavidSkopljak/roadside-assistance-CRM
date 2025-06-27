package com.davidskopljak.skopljakzavrsni.exceptions;

public class EmptyResultSetException extends RuntimeException {
  public EmptyResultSetException() {
  }

  public EmptyResultSetException(String message) {
    super(message);
  }
}
