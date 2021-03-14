package com.ironhack.eventservice.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.ironhack.eventservice.dto.EventsDTO;
import com.ironhack.eventservice.enums.EventStatus;
import com.ironhack.eventservice.enums.RegistrationStatus;
import com.ironhack.eventservice.enums.Visibility;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Events {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long hostId;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime opening;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime ending;
    private String description;
    private Integer guestLimit; // visitors per row allowed
    private Integer currentTotalGuests; // number of total guests participating
    private Integer totalGuestLimit; // limit of participants
    private Integer currentQueuePosition;
    @Enumerated(EnumType.STRING)
    private Visibility visibility;
    @Enumerated(EnumType.STRING)
    private EventStatus eventStatus;
    @Enumerated(EnumType.STRING)
    private RegistrationStatus registrationStatus;
    private String entryCode;

    @OneToMany(mappedBy = "event", fetch = FetchType.EAGER)
    private List<Guest> guestList;

    public Events() {
        setEventStatus(EventStatus.NOT_STARTED);
        setCurrentTotalGuests(0);
        setCurrentQueuePosition(0);
    }

    public static Events parseFromDTO(EventsDTO eventsDTO){
        Events event = new Events();
        event.setName(eventsDTO.getName());
        event.setHostId(eventsDTO.getHostId());
        event.setOpening(eventsDTO.getOpening());
        event.setEnding(eventsDTO.getEnding());
        event.setDescription(eventsDTO.getDescription());
        event.setGuestLimit(eventsDTO.getGuestLimit());
        event.setTotalGuestLimit(eventsDTO.getTotalGuestLimit());
        event.setVisibility(Visibility.valueOf(eventsDTO.getVisibility()));

        if(eventsDTO.getCurrentTotalGuests()!=null) event.setCurrentTotalGuests(eventsDTO.getCurrentTotalGuests());
        else event.setCurrentTotalGuests(0);

        if(eventsDTO.getEventStatus()!=null) event.setEventStatus(EventStatus.valueOf(eventsDTO.getEventStatus()));

        if(eventsDTO.getRegistrationStatus()!=null) event.setRegistrationStatus(RegistrationStatus.valueOf(eventsDTO.getRegistrationStatus()));

        event.setEntryCode(eventsDTO.getEntryCode());

        return event;
    }

    public static Events updateFromDTO(Events event, EventsDTO eventsDTO){
        event.setName(eventsDTO.getName());
        event.setHostId(eventsDTO.getHostId());
        event.setOpening(eventsDTO.getOpening());
        event.setEnding(eventsDTO.getEnding());
        event.setDescription(eventsDTO.getDescription());
        event.setGuestLimit(eventsDTO.getGuestLimit());
        event.setTotalGuestLimit(eventsDTO.getTotalGuestLimit());
        event.setVisibility(Visibility.valueOf(eventsDTO.getVisibility()));

        if(eventsDTO.getCurrentTotalGuests()!=null) event.setCurrentTotalGuests(eventsDTO.getCurrentTotalGuests());
        else event.setCurrentTotalGuests(0);

        if(eventsDTO.getEventStatus()!=null) event.setEventStatus(EventStatus.valueOf(eventsDTO.getEventStatus()));

        if(eventsDTO.getRegistrationStatus()!=null) event.setRegistrationStatus(RegistrationStatus.valueOf(eventsDTO.getRegistrationStatus()));

        event.setEntryCode(eventsDTO.getEntryCode());

        return event;
    }

    public void addGuest(Guest guest){
        this.guestList.add(guest);
    }

    public void popGuest(Guest guest){
        this.guestList.remove(guest);
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

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public EventStatus getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(EventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }

    public RegistrationStatus getRegistrationStatus() {
        return registrationStatus;
    }

    public void setRegistrationStatus(RegistrationStatus registrationStatus) {
        this.registrationStatus = registrationStatus;
    }

    public String getEntryCode() {
        return entryCode;
    }

    public void setEntryCode(String entryCode) {
        this.entryCode = entryCode;
    }

    public List<Guest> getGuestList() {
        return guestList;
    }

    public void setGuestList(List<Guest> guestList) {
        this.guestList = guestList;
    }
}
