import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler } from '@angular/common/http';

@Injectable()
export class BasicAuthInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler) {
    const username = 'alexistdev@gmail.com';
    const password = '325339';
    const authToken = btoa(`${username}:${password}`);

    const authReq = req.clone({
      setHeaders: {
        Authorization: `Basic ${authToken}`
      }
    });

    return next.handle(authReq);
  }
}
