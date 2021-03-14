import { Component, HostBinding, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UsersService } from 'src/app/auth/users.service';
import { ThemeService } from 'src/app/services/theme.service';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent {
  

  constructor(
    public usersService: UsersService,
    public themeService: ThemeService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.themeService.darkTheme.setValue(localStorage.getItem('theme') === "Dark" ? true:false);
  }

  logout(){
    this.usersService.deleteToken();
    this.router.navigate(['/login']);
  }

}

