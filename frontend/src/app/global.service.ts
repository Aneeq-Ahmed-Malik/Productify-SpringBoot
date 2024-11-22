import { Injectable } from '@angular/core';
import { DataService } from './data.service';
@Injectable({
  providedIn: 'root'
})
export class GlobalService {
  cart:any=[];
  cartIDs:any=[];
  recent:any=[];
  
  size=0;
  recentflag:boolean=false;
  constructor(private data:DataService) { }
  addToCart(product:any){
    this.cart.push(product);
    console.log(this.cart);
    this.cartIDs.push(product.id);
  
  }
  addToRecent(product:any){
    this.recent.push(product);
    this.recentflag=true;
  }


 
  increament(){
    this.size++;
  }
  decreament(){
    if(this.size>0)
      this.size--;
  }
}
