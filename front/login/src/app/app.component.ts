import { ChangeDetectorRef, Component, HostBinding, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Router } from '@angular/router';
import { UsersService } from './auth/users.service';
import { ThemeService } from './services/theme.service';

@Component({
  selector: 'body',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{
  title = 'ACNH board';

  @HostBinding('class') className = '';

  constructor(
    public themeService: ThemeService,
    private detectorRef: ChangeDetectorRef
  ) { }

  ngOnInit(): void {
    this.themeService.darkTheme.valueChanges.subscribe((darkMode) => {
      const darkClassName = 'dark-theme-mode';
      this.className = darkMode ? darkClassName : '';
    })
    this.detectorRef.detectChanges()
  }


}

