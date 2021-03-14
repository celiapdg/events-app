package com.ironhack.eventservice.enums;

public enum Visibility {
    PUBLIC("public"),
    PRIVATE("private");

    private String name;

    Visibility(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
