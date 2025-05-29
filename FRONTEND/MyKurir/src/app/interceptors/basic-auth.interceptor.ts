import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler } from '@angular/common/http';
import {LocalStorageService} from "../utils/local-storage.service";


@Injectable()
export class BasicAuthInterceptor implements HttpInterceptor {
  constructor(private localStorageService: LocalStorageService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler) {

    const username = this.localStorageService.getItem('email') || '';
    const password = this.localStorageService.decode(this.localStorageService.getItem('keyPs')) || '';
    console.log(password);
    const authToken = btoa(`${username}:${password}`);

    const authReq = req.clone({
      setHeaders: {
        Authorization: `Basic ${authToken}`
      }
    });

    return next.handle(authReq);
  }
}
