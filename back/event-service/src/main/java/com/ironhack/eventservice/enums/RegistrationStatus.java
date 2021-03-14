package com.ironhack.eventservice.enums;

public enum RegistrationStatus {
    OPEN("open"),
    CLOSED("closed"),
    CLOSED_BY_HOST("closed_by_host");

    private String name;

    RegistrationStatus(String name){
        this.name = name;
    }
}
