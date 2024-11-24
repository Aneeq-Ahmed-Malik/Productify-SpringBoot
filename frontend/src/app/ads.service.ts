import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AdsService {
  private apiUrl = 'http://localhost:8080/api/ad';

  constructor(private http: HttpClient) {}

  postAdWithProgress(formData: FormData): Observable<any> {
    return this.http.post(`${this.apiUrl}/postAd`, formData, {
      reportProgress: true,
      observe: 'events', // Tracks upload progress events
    });
  }
  /////////////////testing////////
  uploadMultipartData(formData: FormData): Observable<any> {
    return this.http.post(this.apiUrl, formData, {
      reportProgress: true,
      observe: 'events', // Tracks upload progress events
      responseType: 'json', // Ensures the response is treated as JSON
    });
  }
  
  
}
