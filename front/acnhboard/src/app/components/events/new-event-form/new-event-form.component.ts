import { Component, HostBinding, Inject, Input, OnInit } from '@angular/core';
import { FormGroup, FormControl, Validators, FormGroupDirective } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { UsersService } from 'src/app/auth/users.service';
import { Events } from 'src/app/models/events';
import { CustomValidators } from 'src/app/utils/custom-validators';
import { BulletinBoardComponent } from '../../bulletin-board/bulletin-board.component';
import { EventDetailsComponent } from '../event-details/event-details.component';

@Component({
  selector: 'app-new-event-form',
  templateUrl: './new-event-form.component.html',
  styleUrls: ['./new-event-form.component.css']
})
export class NewEventFormComponent implements OnInit {

    form: FormGroup;
    minDate: String = new Date().toISOString().substring(0, 16);
  
    nameField: FormControl;
    startField: FormControl;
    endField: FormControl;
    descriptionField: FormControl;
    visibilityField: FormControl;
    guestsField: FormControl;
    totalGuestsField: FormControl;
    
    errorMessage: string = '';
  
    constructor(public dialogRefNew: MatDialogRef<BulletinBoardComponent>,
      public dialogRefEdit: MatDialogRef<EventDetailsComponent>,
      @Inject(MAT_DIALOG_DATA) public data: {title: string, body: Events, mainbutton: string}, //tipo de dato
      public usersService: UsersService) {
          // Initialize Form Control fields
          this.nameField = new FormControl(this.data.body?.name||'', [ Validators.required, Validators.minLength(3), Validators.maxLength(20)]);
          this.startField = new FormControl(this.data.body?.opening||'', [Validators.required]);
          this.endField = new FormControl(this.data.body?.ending||'', [ Validators.required, CustomValidators.dateNotPast, CustomValidators.endAfterStart('start') ]);
          this.descriptionField = new FormControl(this.data.body?.description||'', [ Validators.required]);
          this.visibilityField = new FormControl(this.data.body?.visibility||'', [Validators.required]);
          this.guestsField = new FormControl(this.data.body?.guestLimit||0, [ Validators.required, Validators.max(8), Validators.min(0) ]);
          this.totalGuestsField = new FormControl(this.data.body?.totalGuestLimit||0, [ Validators.required, CustomValidators.minTotalGuests('guests'), Validators.max(1000000)]);
  
          // Initialize Form Group
          this.form = new FormGroup({
            name: this.nameField,
            start: this.startField,
            end: this.endField,
            description: this.descriptionField,
            visibility: this.visibilityField,
            guests: this.guestsField,
            totalGuests: this.totalGuestsField,
          });
  
          this.form.controls.guests.valueChanges.subscribe(() => {
            this.form.controls.totalGuests.updateValueAndValidity();
          });

          this.form.controls.start.valueChanges.subscribe(() => {
            this.form.controls.end.updateValueAndValidity();
          });
    }

  ngOnInit(): void {
  }

  onNoClick(): void {
    this.dialogRefNew.close();
    this.dialogRefEdit.close();
  }

  saveContent(formDirective: FormGroupDirective) {
    
    let formEvent: Events = new Events();

    if (this.data.body !== undefined){
      formEvent = this.data.body;
    }else{
      formEvent.eventStatus = 'NOT_STARTED';
      formEvent.currentTotalGuests = 0;
      formEvent.currentQueuePosition = 0;
      formEvent.hostName = this.usersService.getUsername();
      formEvent.entryCode = '';
    }

    formEvent.visibility = this.visibilityField.value;
    if(this.visibilityField.value === 'PUBLIC'&&this.data.body?.registrationStatus!=='CLOSED_BY_HOST'){
      formEvent.registrationStatus = 'CLOSED';
      if (this.totalGuestsField.value>this.guestsField.value){
        formEvent.registrationStatus = 'OPEN';
      }
    }else if (this.visibilityField.value === 'PRIVATE'){
      formEvent.registrationStatus = 'CLOSED_BY_HOST';
    }

    formEvent.name = this.nameField.value;
    formEvent.opening = this.startField.value;
    formEvent.ending = this.endField.value;
    formEvent.guestLimit = this.guestsField.value;
    formEvent.totalGuestLimit = this.totalGuestsField.value;
    formEvent.description = this.descriptionField.value;

    this.dialogRefNew.close(formEvent);
    this.dialogRefEdit.close(formEvent);
}

}
