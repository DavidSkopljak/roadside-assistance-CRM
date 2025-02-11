package com.davidskopljak.skopljakzavrsni.interfaces;

public interface Trackable<T> {
    T getState();
    void updateState(T state);
}
