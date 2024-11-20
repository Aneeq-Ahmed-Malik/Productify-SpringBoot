import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class GlobalService {
  cart:any=[];

  constructor() { }
  addToCart(product:any){
    this.cart.push(product);
    console.log(this.cart);
    
  }
}
