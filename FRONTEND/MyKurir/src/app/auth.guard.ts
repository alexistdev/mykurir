import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';

import {LocalStorageService} from "./utils/local-storage.service";

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(private router: Router,private localStorageService:LocalStorageService) {}

  canActivate(): boolean {
    const userId = this.localStorageService.getItem('userId');
    const email = this.localStorageService.getItem('email');
    const keyPs = this.localStorageService.getItem('keyPs');
    const role = this.localStorageService.getItem('role');
    if (userId && email && keyPs && role) {
      return true; // Allow access
    } else {
      this.router.navigate(['/login']);
      return false; // Block access
    }
  }


}
