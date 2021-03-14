import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { UsersService } from './users.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private usersService: UsersService, private router: Router) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    console.log('AuthGuard#canActivate called');
    return this.checkLogin(state.url);
  }

  checkLogin(url: string): true|UrlTree{
    if (!this.usersService.authenticated()){
      if (url.indexOf('login') === -1 && url.indexOf('signup') === -1 && url.indexOf('home') === -1){
        return this.router.parseUrl('/login');
      }else{
        return true;
      }
    
    }else{
      // si la pagina es la de login o signup te redirige al tablón
      if (url.indexOf('login') !== -1 || url.indexOf('signup') !== -1 || url.indexOf('home') !== -1){
        return this.router.parseUrl('/bulletin-board');
      }else{
        return true;
      }
    }
  }
}
