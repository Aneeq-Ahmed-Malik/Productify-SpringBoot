import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AdsService {
  private apiUrl = 'http://localhost:8080/api/ad';

  constructor(private http: HttpClient) { }

  postAdWithProgress(formData: FormData): Observable<any> {
    return this.http.post(`${this.apiUrl}/postAd`, formData, {
      reportProgress: true,
      observe: 'events', // Tracks upload progress events
    });
  }
  editAdWithProgress(formData: FormData): Observable<any> {
    return this.http.put(`${this.apiUrl}/editAd`, formData, {
      reportProgress: true,
      observe: 'events', // Tracks upload progress events
    });
  }
  getAllAds(): Observable<any> {
    return this.http.get(`${this.apiUrl}/getAllAds`, {
      headers: { 'Content-Type': 'application/json' },
    });
  }
  getAdsByUserId(userId: number): Observable<any[]> {
    const params = new HttpParams().set('user_id', userId.toString());
    return this.http.get<any[]>(`${this.apiUrl}/getAdsById`, { params });
  }
  deleteAd(userId: number, adId: number): Observable<string> {
    const params = new HttpParams()
      .set('userId', userId)
      .set('ad_id', adId);

    return this.http.delete<string>(`${this.apiUrl}/deleteAd`, {
      params,
      responseType: 'text' as 'json', // Specify that the response is plain text
    });
  }
  checkFeatureAvailability(userId: number): Observable<boolean> {
    return this.http.get<boolean>(`${this.apiUrl}/checkFeatureAvailability`, {
      params: { userId: userId.toString() },
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
