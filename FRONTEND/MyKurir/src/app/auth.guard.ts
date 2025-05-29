import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot} from '@angular/router';

import {LocalStorageService} from "./utils/local-storage.service";

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router,private localStorageService:LocalStorageService) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    const isAuthenticated = this.isAuthenticated();

    if (state.url === '/login') {
      if (isAuthenticated) {
        this.router.navigate(['/admin']);
        return false;
      }
      return true;
    }

    if (isAuthenticated) {
      return true;
    } else {
      this.router.navigate(['/login']);
      return false;
    }
  }

  private isAuthenticated(): boolean {
    const userId = this.localStorageService.getItem('userId');
    const email = this.localStorageService.getItem('email');
    const keyPs = this.localStorageService.getItem('keyPs');
    const role = this.localStorageService.getItem('role');
    return !!(userId && email && keyPs && role);
  }


}
