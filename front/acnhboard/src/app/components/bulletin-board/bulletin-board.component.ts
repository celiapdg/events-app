import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { UsersService } from 'src/app/auth/users.service';
import { Events } from 'src/app/models/events';
import { EventsService } from 'src/app/services/events.service';
import { NewEventFormComponent } from '../events/new-event-form/new-event-form.component';

@Component({
  selector: 'app-bulletin-board',
  templateUrl: './bulletin-board.component.html',
  styleUrls: ['./bulletin-board.component.css']
})
export class BulletinBoardComponent implements OnInit {

  events: Events[];
  started: boolean = true;
  notStarted: boolean = false;
  closed: boolean = true;
  open: boolean = false;

  constructor(public eventsService: EventsService,
    public usersService: UsersService,
    public dialog: MatDialog) { }

  ngOnInit(): void {
    this.getEventsBoard();
    this.started = localStorage.getItem('started') !== 'false';
    this.notStarted = localStorage.getItem('notStarted') !== 'false';
    this.closed = localStorage.getItem('closed') !== 'false';
    this.open = localStorage.getItem('open') !== 'false';
  }

  getEventsBoard():void{
    this.eventsService.getEventsBoard().subscribe(result => {
      this.events = result;
    })
  }

  openDialog(): void {
    const dialogRefNew = this.dialog.open(NewEventFormComponent, {
      width: '600px',
      data: {title: 'New Event', body: undefined, mainbutton: 'CREATE!'}
    });

    dialogRefNew.afterClosed().subscribe(result => {
      if (result!==undefined){
        this.eventsService.postEvent(result).subscribe(newEvent => {
        console.log(newEvent);
        this.getEventsBoard();
      })
      }
    });
  }

  storeShowStartedSelection(){
    localStorage.setItem('started', this.started ? 'true' : 'false');
  }

  storeShowNotStartedSelection(){
    localStorage.setItem('notStarted', this.notStarted ? 'true' : 'false');
  }

  storeShowClosedSelection(){
    localStorage.setItem('closed', this.closed ? 'true' : 'false');
  }
  
  storeShowOpenSelection(){
    localStorage.setItem('open', this.open ? 'true' : 'false');
  }



}
