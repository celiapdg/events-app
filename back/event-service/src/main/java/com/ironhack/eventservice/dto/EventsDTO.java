package com.ironhack.eventservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ironhack.eventservice.model.Events;

import java.time.LocalDateTime;

public class EventsDTO {
    private Long id;
    private String name;
    private Long hostId;
    private String hostName;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime opening;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime ending;
    private String description;
    private Integer guestLimit;
    private Integer currentTotalGuests;
    private Integer totalGuestLimit;
    private Integer currentQueuePosition;
    private String visibility;
    private String eventStatus;
    private String registrationStatus;
    private String entryCode;

    /** creates a eventsDTO retrieving the info from an event */
    public static EventsDTO parseFromEvent(Events events){
        EventsDTO eventsDTO = new EventsDTO();
        eventsDTO.setId(events.getId());
        eventsDTO.setName(events.getName());
        eventsDTO.setHostId(events.getHostId());
        eventsDTO.setOpening(events.getOpening());
        eventsDTO.setEnding(events.getEnding());
        eventsDTO.setDescription(events.getDescription());
        eventsDTO.setGuestLimit(events.getGuestLimit());
        eventsDTO.setCurrentTotalGuests(events.getCurrentTotalGuests());
        eventsDTO.setTotalGuestLimit(events.getTotalGuestLimit());
        eventsDTO.setCurrentQueuePosition(events.getCurrentQueuePosition());
        eventsDTO.setVisibility(events.getVisibility().toString());
        eventsDTO.setEventStatus(events.getEventStatus().toString());
        eventsDTO.setRegistrationStatus(events.getRegistrationStatus().toString());
        eventsDTO.setEntryCode(events.getEntryCode());

        return eventsDTO;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public LocalDateTime getOpening() {
        return opening;
    }

    public void setOpening(LocalDateTime opening) {
        this.opening = opening;
    }

    public LocalDateTime getEnding() {
        return ending;
    }

    public void setEnding(LocalDateTime ending) {
        this.ending = ending;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getGuestLimit() {
        return guestLimit;
    }

    public void setGuestLimit(Integer guestLimit) {
        this.guestLimit = guestLimit;
    }

    public Integer getCurrentTotalGuests() {
        return currentTotalGuests;
    }

    public void setCurrentTotalGuests(Integer currentTotalGuests) {
        this.currentTotalGuests = currentTotalGuests;
    }

    public Integer getTotalGuestLimit() {
        return totalGuestLimit;
    }

    public void setTotalGuestLimit(Integer totalGuestLimit) {
        this.totalGuestLimit = totalGuestLimit;
    }

    public Integer getCurrentQueuePosition() {
        return currentQueuePosition;
    }

    public void setCurrentQueuePosition(Integer currentQueuePosition) {
        this.currentQueuePosition = currentQueuePosition;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(String registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public String getEntryCode() {
        return entryCode;
    }

    public void setEntryCode(String entryCode) {
        this.entryCode = entryCode;
    }
}
