package com.ironhack.eventservice.enums;

public enum EventStatus {
    NOT_STARTED("not_started"),
    PENDING_CODE("pending_code"),
    STARTED("started"),
    FINISHED("finished"),
    CANCELLED("cancelled");

    private String name;

    EventStatus(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
