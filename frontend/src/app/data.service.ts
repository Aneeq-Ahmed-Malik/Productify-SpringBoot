import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class DataService {
  private baseUrl = 'http://localhost:8080/api/products';

  constructor(private http: HttpClient) {}

  getAllProducts(): Observable<any> {
    return this.http.get(`${this.baseUrl}/allproducts`);
  }

  getProductsByCategory(categoryName: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/category/${categoryName}`);
  }
  getProductsByWebsite(websiteName: string): Observable<any> {
    return this.http.get(`${this.baseUrl}/website/${websiteName}`);
  }
  getProductsByCatWeb(category:string,websiteName:string): Observable<any>{
    return this.http.get(`${this.baseUrl}/${category}/${websiteName}`); 
  }
  
}
