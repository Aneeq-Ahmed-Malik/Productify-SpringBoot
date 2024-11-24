import { Component, OnDestroy, OnInit } from '@angular/core';
import { GlobalService } from '../global.service';
import { CartService } from '../cart.service';
@Component({
  selector: 'app-cart',
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.scss'
})
export class CartComponent  implements OnInit {
  constructor(private global:GlobalService){}
  
  cart:any=[];
  ngOnInit(){
    this.cart=this.global.cart;
  }
  Remove(index: any, product: any) {
    // Remove the product from the cart
    this.cart.splice(index, 1);
  
    // Update the global cart
    this.global.cart = this.cart;
  
    // Remove the product ID from the productIDs array
    const productIndex = this.global.productIDs.indexOf(product.id);
    if (productIndex > -1) {
      this.global.productIDs.splice(productIndex, 1);
    }
  
    // Call the global service to remove the product from the server
    this.global.removefromCart(product);
  
    // Decrement the cart size
    this.global.decreament();
  
    console.log('Updated cart:', this.cart);
    console.log('Updated product IDs:', this.global.productIDs);
  }
  
  redirectTo(link: string) {
   // window.location.href = link; // Opens the link in the same tab
    window.open(link, '_blank'); // Opens the link in a new tab

  }
  getsize(): boolean {
    return this.cart && this.cart.length > 0;
}

}
