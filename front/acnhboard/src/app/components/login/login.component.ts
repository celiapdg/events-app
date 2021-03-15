import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup, FormGroupDirective, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { UsersService } from 'src/app/auth/users.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username: string;
  password: string;

  form: FormGroup;

  usernameField: FormControl;
  passwordField: FormControl;

  invalidLogin = false;
  errorMessage = 'Invalid Username or Password';

  constructor(
    public usersService: UsersService,
    private route: ActivatedRoute,
    private router: Router) {
        // Initialize Form Control fields
        this.usernameField = new FormControl('', [ Validators.required ]);
        this.passwordField = new FormControl('', [ Validators.required ]);
    
        // Initialize Form Group
        this.form = new FormGroup({
          username: this.usernameField,
          password: this.passwordField,
        });
  }

  login(formDirective: FormGroupDirective) {
    const user = {username: this.usernameField.value.toLocaleLowerCase(), password: this.passwordField.value};
    this.usersService.login(user).subscribe( data => {
      this.usersService.setToken(data.jwt, this.usernameField.value.toLocaleLowerCase());
      this.invalidLogin = false;
      this.router.navigate(['bulletin-board']);
    }, () => {
      this.invalidLogin = true;
    }
    );
  }
}
