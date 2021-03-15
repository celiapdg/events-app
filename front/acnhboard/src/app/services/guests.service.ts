import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Guest } from '../models/guest';

@Injectable({
  providedIn: 'root'
})
export class GuestsService {

  baseUrl: string = "http://localhost:8080"

  constructor(private http: HttpClient) {}

  joinEvent(eventId: number): Observable<Guest> {
    return this.http.post<Guest>(this.baseUrl+"/guests/"+eventId, null);
  }

  getGuestList(eventId: number): Observable<Guest[]>{
    return this.http.get<Guest[]>(this.baseUrl+"/guests/all/"+eventId);
  }
  
  getNextGuests(eventId: number): Observable<Guest[]>{
    return this.http.get<Guest[]>(this.baseUrl+"/guests/next/"+eventId);
  }

  checkGuest(eventId: number): Observable<Guest>{
    return this.http.get<Guest>(this.baseUrl+"/guests/"+eventId);
  }

  kickGuest(guestId: number, eventId: number): Observable<void>{
    return this.http.put<void>(this.baseUrl+"/guests/kick",{guestId: guestId, eventId: eventId});
  }

  leave(eventId: number): Observable<void>{
    return this.http.delete<void>(this.baseUrl+"/guests/leave/"+eventId);
  }

  guestReady(eventId: number): Observable<void>{
    return this.http.put<void>(this.baseUrl+"/guests/ready/"+eventId, {});
  }

  guestVisiting(eventId: number): Observable<void>{
    return this.http.put<void>(this.baseUrl+"/guests/visiting/"+eventId, {});
  }

  guestVisited(eventId: number): Observable<void>{
    return this.http.put<void>(this.baseUrl+"/guests/visited/"+eventId, {});
  }

}
