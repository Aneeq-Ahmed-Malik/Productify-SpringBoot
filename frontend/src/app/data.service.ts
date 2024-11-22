import { Injectable } from '@angular/core';
import { HttpClient,HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class DataService {
  private baseUrlProducts = 'http://localhost:8080/api/products';
  private baseUrlRecomend = 'http://localhost:8080/api/recommend'; // API base URL

  constructor(private http: HttpClient) {}

  getAllProducts(): Observable<any> {
    return this.http.get(`${this.baseUrlProducts}/allproducts`);
  }

  getProductsByCategory(categoryName: string): Observable<any> {
    return this.http.get(`${this.baseUrlProducts}/category/${categoryName}`);
  }
  getProductsByWebsite(websiteName: string): Observable<any> {
    return this.http.get(`${this.baseUrlProducts}/website/${websiteName}`);
  }
  getProductsByCatWeb(category:string,websiteName:string): Observable<any>{
    return this.http.get(`${this.baseUrlProducts}/${category}/${websiteName}`); 
  }
  getRecommendations(productIds: number[], limit: number = 6): Observable<any> {
    // Prepare HTTP query parameters
    let params = new HttpParams()
      .set('productIds', productIds.join(',')) // Join product IDs into a comma-separated string
      .set('limit', limit); // Keep limit as a number
  
    return this.http.get(`${this.baseUrlRecomend}/get`, { params });
  }
  
}
