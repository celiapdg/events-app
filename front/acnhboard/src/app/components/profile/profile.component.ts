import { Component, OnInit } from '@angular/core';
import { UsersService } from 'src/app/auth/users.service';
import { EventsService } from 'src/app/services/events.service';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {

  eventsHost: number;
  eventsGuest: number;

  constructor(
    public usersService: UsersService,
    private eventsService: EventsService
  ) { }

  ngOnInit(): void {
    this.getEventAsHost();
    this.getEventsAsGuest();
  }


  getEventAsHost(): void{
    this.eventsService.getEventAsHost().subscribe(result => {
      console.log(result)
      this.eventsHost = result.length;
    })
  }

  getEventsAsGuest(): void{
    this.eventsService.getEventAsGuest().subscribe(result => {
      console.log(result)
      this.eventsGuest = result.length;
    })
  }
}
