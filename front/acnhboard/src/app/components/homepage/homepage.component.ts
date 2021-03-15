import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { EventsService } from 'src/app/services/events.service';
import { UsersService } from 'src/app/auth/users.service';
import { Events } from 'src/app/models/events';
import { Router } from '@angular/router';

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent implements OnInit {

  events: Events[];
  started: Events[];

  @ViewChild('widgetsContent1') widgetsContent1: ElementRef;
  @ViewChild('widgetsContent2') widgetsContent2: ElementRef;

  constructor(public eventsService: EventsService,
    public usersService: UsersService,
    private router: Router) { }

  ngOnInit(): void {
    this.getEventsBoard();
    this.getEventsStarted();
  }

  getEventsBoard():void{
    this.eventsService.getEventsBoard().subscribe(result =>
      this.events = result)
  }

  getEventsStarted():void{
    this.eventsService.getEventsStarted().subscribe(result => {
      this.started = result;
    })
  }

  scrollLeft1(){
    this.widgetsContent1.nativeElement.scrollLeft -= 320;
  }
  
  scrollRight1(){
    this.widgetsContent1.nativeElement.scrollLeft += 320;
  }

  scrollLeft2(){
    this.widgetsContent2.nativeElement.scrollLeft -= 320;
  }
  
  scrollRight2(){
    this.widgetsContent2.nativeElement.scrollLeft += 320;
  }

}
