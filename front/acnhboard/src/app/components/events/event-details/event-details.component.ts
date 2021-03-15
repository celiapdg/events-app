import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { UsersService } from 'src/app/auth/users.service';
import { Events } from 'src/app/models/events';
import { Guest } from 'src/app/models/guest';
import { EventsService } from 'src/app/services/events.service';
import { GuestsService } from 'src/app/services/guests.service';
import { InputEntrycodeComponent } from '../input-entrycode/input-entrycode.component';
import { NewEventFormComponent } from '../new-event-form/new-event-form.component';

@Component({
  selector: 'app-event-details',
  templateUrl: './event-details.component.html',
  styleUrls: ['./event-details.component.css']
})
export class EventDetailsComponent implements OnInit {

  isEventLoaded: Promise<boolean>;
  event: Events;
  errorMessage: string = '';
  isHost: boolean = false;
  guests: Guest[];

  isGuest: boolean = false;
  currentGuest: Guest;
  canGuestViewCode: boolean = false;
  guestShowCode: boolean = false;

  registered: boolean = true;
  ready: boolean = true;
  visiting: boolean = true;
  visited: boolean = true;
  kicked: boolean = true;

  constructor(
    private eventsService: EventsService,
    private guestsService: GuestsService,
    public usersService: UsersService,
    private activatedRoute: ActivatedRoute,
    private _location: Location,
    public dialog: MatDialog
  ) { }

  ngOnInit(): void {
    this.registered = localStorage.getItem('registered') !== 'false';
    this.ready = localStorage.getItem('ready') !== 'false';
    this.visiting = localStorage.getItem('visiting') !== 'false';
    this.visited = localStorage.getItem('visited') !== 'false';
    this.kicked = localStorage.getItem('kicked') !== 'false';
    this.setUp();
  }

  setUp(): void {
    this.activatedRoute.params.subscribe(params => {
      const postId = +params.eventId;
      this.eventsService.getEventById(postId).subscribe(event => {
        this.event = event;
        this.checkUser();
      },
        (err) => { this.errorMessage = err.error }
      )
    }
    )
  };

  private checkUser() {
    if (this.event.hostName === this.usersService.getUsername()) {
      this.isHost = true;
      this.isGuest = false;
      this.getGuestList();
    }
    else {
      this.isHost = false;
      this.checkGuest();
    }


  }

  private getGuestList(): void {
    this.guestsService.getGuestList(this.event.id).subscribe(result => {
      this.guests = result;
    },
      error => {
      });
  }

  private checkGuest(): void {
    this.guestsService.checkGuest(this.event.id).subscribe(result => {
      if (result !== null) {
        this.isGuest = true;
        this.currentGuest = result;
        this.canViewCode();
      } else {
        this.isGuest = false;
      }
    });
  }

  private canViewCode(): boolean | void {
    this.guestsService.getNextGuests(this.event.id).subscribe(result => {
      result.forEach(guest => {
        if (guest.username === this.usersService.getUsername()){
          this.canGuestViewCode = true;
        }
      })
    })
    
  }

  back() {
    this._location.back();
  }

  join(): void {
    this.guestsService.joinEvent(this.event.id).subscribe(result => {
      this.isGuest = true;
      this.setUp();
    },
      error => {
        console.log(error)
      });
  }

  cancel(): void {
    this.eventsService.cancelEvent(this.event.id).subscribe(result =>
      this.setUp())
  }

  closeRegistration(): void {
    this.eventsService.closeEvent(this.event.id).subscribe(result =>
      this.setUp())
  }

  openRegistration(): void {
    this.eventsService.openEvent(this.event.id).subscribe(result =>
      this.setUp())
  }

  kick(guestId: number): void {
    this.guestsService.kickGuest(guestId, this.event.id).subscribe(result =>
      this.setUp())
  }

  leave(): void {
    if (this.currentGuest.status !== 'VISITING') {
      this.guestsService.leave(this.event.id).subscribe(result => {
        this.setUp();
        this.isGuest = false;
      })
    } else {
      this.currentGuest.status = 'VISITED';
      this.guestsService.guestVisited(this.event.id).subscribe(() => {
        this.setUp();
        this.guestShowCode = false;
      })
  }
  this.canGuestViewCode = false;
  }

  beReady(): void {
    this.currentGuest.status = 'READY';
    this.guestsService.guestReady(this.event.id).subscribe(() => 
      this.setUp()
    )
  }

  showCode(): void {
    this.guestShowCode = true;
    this.currentGuest.status = 'VISITING';
    this.guestsService.guestVisiting(this.event.id).subscribe(() => 
      this.setUp())
  }

  openEditDialog(): void {
    const dialogRefEdit = this.dialog.open(NewEventFormComponent, {
      // panelClass: 'dark-theme-mode',
      width: '600px',
      data: { title: this.event.name.toLocaleUpperCase(), body: this.event, mainbutton: 'SAVE!' }
    });

    dialogRefEdit.afterClosed().subscribe(result => {
      if (result !== undefined) {
        this.eventsService.putEvent(this.event.id, result).subscribe(editedEvent => {
          this.event = editedEvent;
        })
      }
    });
  }

  openCodeDialog(): void {
    const dialogRefCode = this.dialog.open(InputEntrycodeComponent, {
      // panelClass: 'dark-theme-mode',
      width: '400px',
      height: '200px',
      data: { title: this.event.name.toLocaleUpperCase(), body: this.event.entryCode }
    });

    dialogRefCode.afterClosed().subscribe(result => {
      if (result !== undefined && result.length === 6) {
        this.event.entryCode = result;
        this.eventsService.putEvent(this.event.id, this.event).subscribe(editedEvent => {
          this.event = editedEvent;
        })
      }
    });
  }

  storeRegisteredSelection(): void{
    localStorage.setItem('registered', this.registered ? 'true' : 'false');
  }

  
  storeReadySelection(): void{
    localStorage.setItem('ready', this.ready ? 'true' : 'false');
  }

  
  storeVisitingSelection(): void{
    localStorage.setItem('visiting', this.visiting ? 'true' : 'false');
  }

  
  storeVisitedSelection(): void{
    localStorage.setItem('visited', this.visited ? 'true' : 'false');
  }


  storeKickedSelection(): void{
    localStorage.setItem('kicked', this.kicked ? 'true' : 'false');
  }

}
