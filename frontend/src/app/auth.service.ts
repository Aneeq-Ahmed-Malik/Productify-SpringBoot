import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiBaseUrl = 'http://localhost:8080/api/user';

  constructor(private http: HttpClient) {}

  signup(user: { email: string; password: string; name: string }): Observable<any> {
    return this.http.post(`${this.apiBaseUrl}/signup`, user, {
      headers: { 'Content-Type': 'application/json' },
    });
  }

  login(user: { email: string; password: string }): Observable<any> {
    return this.http.post(`${this.apiBaseUrl}/login`, user, {
      headers: { 'Content-Type': 'application/json' },
    });
  }
}
