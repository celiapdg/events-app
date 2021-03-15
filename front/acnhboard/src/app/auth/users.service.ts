import { Injectable } from '@angular/core';
import { HttpClient } from "@angular/common/http";
import { Observable } from 'rxjs';
import { CookieService } from 'ngx-cookie-service';

@Injectable({
  providedIn: 'root'
})
export class UsersService {

  constructor(
    private http: HttpClient, 
    private cookies: CookieService) {}

  login(user: {username: string, password: string}): Observable<any> {
    return this.http.post("http://localhost:8080/auth/login", user);
  }

  signup(user: {username: string, email: string, password: string}): Observable<any> {
    return this.http.post("http://localhost:8080/auth/signup", user, {responseType: 'text'});
  }

  setToken(token: string, username: string) {
    this.cookies.set("token", token);
    this.cookies.set("username", username)
    this.cookies.set("authenticated", 'true')
  }

  getToken(): string {
    return this.cookies.get("token");
  }

  getUsername(): string {
    return this.cookies.get("username");
  }

  authenticated(): boolean {
    return this.cookies.get("authenticated") === 'true';
  }

  deleteToken(): void{
    this.cookies.deleteAll();
  }

  
}
