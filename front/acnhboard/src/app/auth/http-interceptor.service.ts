import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UsersService } from './users.service';


@Injectable()
export class HttpInterceptorService implements HttpInterceptor {

    constructor(private usersService: UsersService) { }

    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        if (this.usersService.authenticated() && req.url.indexOf('login') === -1 && req.url.indexOf('signup') === -1) {
          const authReq = req.clone({
                headers: new HttpHeaders({
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + this.usersService.getToken()
                })
            });
            
            return next.handle(authReq);
        } else {
            return next.handle(req);
        }
    }
}