import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormGroupDirective, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UsersService } from 'src/app/auth/users.service';
import { CustomValidators } from 'src/app/utils/custom-validators';

@Component({
  selector: 'app-signup',
  templateUrl: './signup.component.html',
  styleUrls: ['./signup.component.css']
})
export class SignupComponent implements OnInit {
  username: string;
  email: string;
  password: string;
  repeatPassword: string;

  form: FormGroup;

  usernameField: FormControl;
  emailField: FormControl;
  passwordField: FormControl;
  repeatPasswordField: FormControl;
  invalidSignup = false;
  errorMessage: string = '';

  constructor(public usersService: UsersService,
    private router: Router) {
        // Initialize Form Control fields
        this.usernameField = new FormControl('', [ Validators.required, CustomValidators.usernameValidator, Validators.maxLength(30)]);
        this.emailField = new FormControl('', [Validators.required, CustomValidators.emailValidator, Validators.maxLength(100)])
        this.passwordField = new FormControl('', [ Validators.required, CustomValidators.passwordValidator, Validators.maxLength(30)]);
        this.repeatPasswordField = new FormControl('', [ Validators.required, CustomValidators.matchPasswords('password')]);

        // Initialize Form Group
        this.form = new FormGroup({
          username: this.usernameField,
          email: this.emailField,
          password: this.passwordField,
          repeatPassword: this.repeatPasswordField
        });

        this.form.controls.password.valueChanges.subscribe(() => {
          this.form.controls.repeatPassword.updateValueAndValidity();
        });
  }

  ngOnInit(): void {
  }

  signup(formDirective: FormGroupDirective) {
    let username = this.usernameField.value
    const user = {username: this.usernameField.value.toLocaleLowerCase(), email: this.emailField.value, password: this.passwordField.value};
    this.usersService.signup(user).subscribe( () => {
      this.invalidSignup = false;
      this.router.navigate(['/login']);
    }, error => {
      this.invalidSignup = true;
      this.errorMessage = error.error
    }
    );
}

}
