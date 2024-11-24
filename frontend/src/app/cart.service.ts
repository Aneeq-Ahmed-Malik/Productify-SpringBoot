import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CartService {
  private apiUrl = 'http://localhost:8080/api/cart';

  constructor(private http: HttpClient) {}

  getCart(userId: any): Observable<any> {
    const params = new HttpParams().set('user_id', userId); // Pass user_id as query parameter
    return this.http.get(`${this.apiUrl}/getProductsInCart`, { params });
  }
  addToCart(userId: number, productId: any) {
    const params = { user_id: userId, product_id: productId };
    return this.http.post(`${this.apiUrl}/addToCart`, {}, {
      params,
      responseType: 'text' // To handle plain text response like "Product added successfully"
    });
  }
  deleteFromCart(userId: any, productId: any): Observable<any> {
    const params = { user_id: userId, product_id: productId };
    return this.http.delete(`${this.apiUrl}/deleteProductFromCart`, {
      params,
      responseType: 'text', // To handle plain text response like "Product removed successfully"
    });
  }
  
}
