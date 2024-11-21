import { Component, OnInit } from '@angular/core';
import { GlobalService } from '../global.service';
@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.scss'
})
export class CartComponent  implements OnInit{
  constructor(private global:GlobalService){}
  cart:any=[];
  ngOnInit(){
    this.cart=this.global.cart;
  }
  Remove(index:any){
    this.cart.splice(index, 1);
    this.global.cart=this.cart;
    this.global.decreament();
  }
  redirectTo(link: string) {
   // window.location.href = link; // Opens the link in the same tab
    window.open(link, '_blank'); // Opens the link in a new tab

  }
  getsize(): boolean {
    return this.cart && this.cart.length > 0;
}
}
