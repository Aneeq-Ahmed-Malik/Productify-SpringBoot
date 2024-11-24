import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { GlobalService } from './global.service';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(private global: GlobalService, private router: Router) {}

  canActivate(): boolean {
    if (this.global.loginflag) {
      return true; // Allow navigation if logged in
    } else {
      this.router.navigate(['login']); // Redirect to login if not authenticated
      return false; // Block navigation
    }
  }
}
