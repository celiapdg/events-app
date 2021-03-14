package com.ironhack.eventservice.enums;

public enum GuestStatus {
    REGISTERED("registered"),
    READY("ready"),
    VISITING("visiting"),
    VISITED("visited"),
    CANCELLED("cancelled"),
    KICKED_OUT("kicked_out");

    private String name;

    GuestStatus(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
