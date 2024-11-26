import { Injectable } from '@angular/core';
import { HttpClient,HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class DataService {
  private baseUrlProducts = 'http://localhost:8080/api/products';
  private baseUrlRecomend = 'http://localhost:8080/api/recommend'; // API base URL
  private baseUrlReview = 'http://localhost:8080/api/review'; // API base URL

  

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
  searchProducts(query: string, limit: number): Observable<any> {
    // Create query parameters
    const params = new HttpParams()
      .set('query', query)
      .set('limit', limit);

    // Make HTTP GET request
    return this.http.get(`${this.baseUrlProducts}/search`, { params });
  }
  getSentimentAnalysis(link: string): Observable<string> {
    const params = new HttpParams().set('link', link);
    return this.http.get(`${this.baseUrlReview}/sentiment`, {
      params,
      responseType: 'text', // Since the endpoint returns a string
    });

  }
  generateQrCodeUrl(text: string): string {
    return `https://quickchart.io/qr?text=${encodeURIComponent(text)}`;
  }
}
