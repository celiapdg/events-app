import { Injectable } from '@angular/core';
import { FormControl } from '@angular/forms';

@Injectable({
  providedIn: 'root'
})
export class ThemeService {

  darkTheme = new FormControl(false);

  constructor() { }

  // store the value of the theme, so it persists
  storeThemeSelection(){
    localStorage.setItem('theme', this.darkTheme.value ? 'Dark' : 'Light');
  }



}
