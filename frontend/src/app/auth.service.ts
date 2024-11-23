import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiBaseUrl = 'http://localhost:8080/api/user';

  constructor(private http: HttpClient) {}

  signup(user: any): Observable<any> {
    return this.http.post(`${this.apiBaseUrl}/signup`, user,{ responseType: 'text' });
  }

  login(user: any): Observable<any> {
    return this.http.post(`${this.apiBaseUrl}/login`, user);
  }
}
