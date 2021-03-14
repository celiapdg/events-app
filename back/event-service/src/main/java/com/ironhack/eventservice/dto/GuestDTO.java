package com.ironhack.eventservice.dto;

import com.ironhack.eventservice.model.Guest;

public class GuestDTO {
    private Long id;
    private String username;
    private String status;
    private Integer queuePosition;

    /** creates a guestDTO retrieving the info from a guest */
    public static GuestDTO parseFromGuest(Guest guest){
        GuestDTO guestDTO = new GuestDTO();
        guestDTO.setId(guest.getId().getGuestId());
        guestDTO.setStatus(guest.getGuestStatus().toString());
        guestDTO.setQueuePosition(guest.getQueuePosition());

        return guestDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getQueuePosition() {
        return queuePosition;
    }

    public void setQueuePosition(Integer queuePosition) {
        this.queuePosition = queuePosition;
    }
}
