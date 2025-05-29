import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler } from '@angular/common/http';
import {LocalStorageService} from "../utils/local-storage.service";


@Injectable()
export class BasicAuthInterceptor implements HttpInterceptor {
  constructor(private localStorageService: LocalStorageService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler) {

    const username: string = this.localStorageService.getItem('email') || '';
    const password: string = this.localStorageService.getItem('keyPs') || '';

    const decodeUsername: string = this.localStorageService.decode(username);
    const decodePassword: string = this.localStorageService.decode(password);

    const authToken: string = btoa(`${decodeUsername}:${decodePassword}`);

    const authReq = req.clone({
      setHeaders: {
        Authorization: `Basic ${authToken}`
      }
    });

    return next.handle(authReq);
  }
}
