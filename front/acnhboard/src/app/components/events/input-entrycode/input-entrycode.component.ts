import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormGroupDirective, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { EventDetailsComponent } from '../event-details/event-details.component';

@Component({
  selector: 'app-input-entrycode',
  templateUrl: './input-entrycode.component.html',
  styleUrls: ['./input-entrycode.component.css']
})
export class InputEntrycodeComponent implements OnInit {
  
  form: FormGroup;
  codeField: FormControl;

  constructor(public dialogRefCode: MatDialogRef<EventDetailsComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {title: string, body: string}) {
      this.codeField = new FormControl(this.data.body, [ Validators.required, Validators.minLength(6), 
                                                         Validators.maxLength(6), Validators.pattern('[A-Z0-9]{6}')]);
     
      this.form = new FormGroup({
        code: this.codeField,
      })
    }

  ngOnInit(): void {
  }

  saveContent(formDirective: FormGroupDirective) {
    this.dialogRefCode.close(this.codeField.value);
}
}
