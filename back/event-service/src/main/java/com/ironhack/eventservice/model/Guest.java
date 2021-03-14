package com.ironhack.eventservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ironhack.eventservice.classes.GuestEventKey;
import com.ironhack.eventservice.enums.GuestStatus;

import javax.persistence.*;

@Entity
public class Guest {

    @EmbeddedId
    GuestEventKey id;

    @ManyToOne
    @MapsId("eventId")
    @JsonIgnore
    private Events event;
    @Enumerated(EnumType.STRING)
    private GuestStatus guestStatus;
    private Integer queuePosition;

    public Guest() {
    }

    public Guest(GuestEventKey id, Events event, GuestStatus guestStatus, Integer queuePosition) {
        this.id = id;
        this.event = event;
        this.guestStatus = guestStatus;
        this.queuePosition = queuePosition;
    }

    public void setId(GuestEventKey id) {
        this.id = id;
    }

    public GuestEventKey getId() {
        return id;
    }

    public Events getEvent() {
        return event;
    }

    public void setEvent(Events event) {
        this.event = event;
    }

    public GuestStatus getGuestStatus() {
        return guestStatus;
    }

    public void setGuestStatus(GuestStatus guestStatus) {
        this.guestStatus = guestStatus;
    }

    public Integer getQueuePosition() {
        return queuePosition;
    }

    public void setQueuePosition(Integer queuePosition) {
        this.queuePosition = queuePosition;
    }
}
