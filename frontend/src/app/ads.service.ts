import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AdsService {
  private apiUrl = 'http://localhost:8080/api/ad';

  constructor(private http: HttpClient) {}

  postAd(formData: FormData): Observable<any> {
    return this.http.post(`${this.apiUrl}/postAd`, formData);
  }
  
}
