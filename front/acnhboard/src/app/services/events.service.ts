import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Events } from '../models/events';

@Injectable({
  providedIn: 'root'
})
export class EventsService {

  baseUrl: string = "http://localhost:8080"

  constructor(private http: HttpClient) {}

  getEventsBoard(): Observable<Events[]> {
    return this.http.get<Events[]>(this.baseUrl + "/events/board");
  }

  getEventsStarted(): Observable<Events[]> {
    return this.http.get<Events[]>(this.baseUrl + "/events/started");
  }


  getEventById(id: number): Observable<Events> {
    return this.http.get<Events>(this.baseUrl+"/events/" + id);
  }

  getEventAsHost(): Observable<Events[]> {
    return this.http.get<Events[]>(this.baseUrl+"/events/host");
  }

  getEventAsGuest(): Observable<Events[]> {
    return this.http.get<Events[]>(this.baseUrl+"/events/guest");
  }

  postEvent(event: Events): Observable<Events> {
    return this.http.post<Events>(this.baseUrl+"/events", this.parseEvent(event));
  }
  
  putEvent(id: number, event: Events): Observable<Events> {
    return this.http.put<Events>(this.baseUrl+"/events/"+id, this.parseEvent(event));
  }

  cancelEvent(id: number): Observable<void>{
    return this.http.put<void>(this.baseUrl+"/events/cancel/"+id,null)
  }

  closeEvent(id: number): Observable<void>{
    return this.http.put<void>(this.baseUrl+"/events/close/"+id,null)
  }
  
  openEvent(id: number): Observable<void>{
    return this.http.put<void>(this.baseUrl+"/events/open/"+id,null)
  }

  private parseEvent(event: Events): {id: number, name: string, 
    hostId: number, hostName: string, opening: Date, ending: Date, 
    description: string, guestLimit: number, currentTotalGuests: number,
    totalGuestLimit: number, currentQueuePosition: number, visibility: string,
    eventStatus: string, registrationStatus: string, entryCode: string}{
    return {
      id: event.id,
      name: event.name,
      hostId: event.hostId,
      hostName: event.hostName,
      opening: event.opening,
      ending: event.ending,
      description: event.description,
      guestLimit: event.guestLimit,
      currentTotalGuests: event.currentTotalGuests,
      totalGuestLimit: event.totalGuestLimit,
      currentQueuePosition: event.currentQueuePosition,
      visibility: event.visibility,
      eventStatus: event.eventStatus,
      registrationStatus: event.registrationStatus,
      entryCode: event.entryCode
      }
  }
}
