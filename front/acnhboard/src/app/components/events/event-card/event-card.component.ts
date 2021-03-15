import { Component, Input, OnInit } from '@angular/core';
import { Events } from 'src/app/models/events';

@Component({
  selector: 'app-event-card',
  templateUrl: './event-card.component.html',
  styleUrls: ['./event-card.component.css']
})
export class EventCardComponent implements OnInit {

  @Input() event: Events;

  constructor() { }

  ngOnInit(): void {
  }

}
