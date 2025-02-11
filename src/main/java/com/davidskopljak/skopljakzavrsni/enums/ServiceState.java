package com.davidskopljak.skopljakzavrsni.enums;

public enum ServiceState {
    ASSIGNED, //service assigned to partner, not accepted yet
    IN_PROGRESS, //service accepted by partner
    FINISHED, //service finished
    CANCELLED, //service cancelled by operator
}
