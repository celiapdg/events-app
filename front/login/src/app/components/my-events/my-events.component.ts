import { Component, OnInit } from '@angular/core';
import * as moment from 'moment';
import { Events } from 'src/app/models/events';
import { EventsService } from 'src/app/services/events.service';

@Component({
  selector: 'app-my-events',
  templateUrl: './my-events.component.html',
  styleUrls: ['./my-events.component.css']
})
export class MyEventsComponent implements OnInit {

  eventsHost: Events[];
  eventsGuest: Events[];

  past: boolean = true;
  incoming: boolean = true;
  private: boolean = true;
  public: boolean = true;

  now = new Date();

  constructor(
    private eventsService: EventsService
  ) { }

  ngOnInit(): void {
    this.past = localStorage.getItem('past') !== 'false';
    this.incoming = localStorage.getItem('incoming') !== 'false';
    this.private = localStorage.getItem('private') !== 'false';
    this.public = localStorage.getItem('public') !== 'false';
    this.getEventAsHost();
    this.getEventsAsGuest();
  }


  getEventAsHost(): void{
    this.eventsService.getEventAsHost().subscribe(result => {
      this.eventsHost = result;
    })
  }

  getEventsAsGuest(): void{
    this.eventsService.getEventAsGuest().subscribe(result => {
      this.eventsGuest = result;
    })
  }

  isPast(date: Date): boolean {
    return moment(date).isBefore(moment(this.now))
  }

  storePastSelection(): void{
    localStorage.setItem('past', this.past ? 'true' : 'false');
  }

  
  storeIncomingSelection(): void{
    localStorage.setItem('incoming', this.incoming ? 'true' : 'false');
  }

  
  storePrivateSelection(): void{
    localStorage.setItem('private', this.private ? 'true' : 'false');
  }

  
  storePublicSelection(): void{
    localStorage.setItem('public', this.public ? 'true' : 'false');
  }

}
