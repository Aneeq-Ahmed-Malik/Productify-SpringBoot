import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class GlobalService {
  cart:any=[];
  recent:any=[];
  size=0;
  constructor() { }
  addToCart(product:any){
    this.cart.push(product);
    console.log(this.cart);
    
  }
  addToRecent(product:any){
    this.recent.push(product);
  }
  increament(){
    this.size++;
  }
  decreament(){
    if(this.size>0)
      this.size--;
  }
}
