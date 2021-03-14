package com.ironhack.edgeservice.classes;

import java.util.Objects;

public class GuestEventKey{

    Long guestId;
    Long eventId;

    public GuestEventKey() {
    }

    public GuestEventKey(Long guestId, Long eventId) {
        this.guestId = guestId;
        this.eventId = eventId;
    }

    public Long getGuestId() {
        return guestId;
    }

    public void setGuestId(Long guestId) {
        this.guestId = guestId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GuestEventKey)) return false;
        GuestEventKey that = (GuestEventKey) o;
        return getGuestId().equals(that.getGuestId()) && getEventId().equals(that.getEventId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getGuestId(), getEventId());
    }
}
