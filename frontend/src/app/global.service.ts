import { Injectable } from '@angular/core';
import { CartService } from './cart.service';

@Injectable({
  providedIn: 'root',
})
export class GlobalService {
  cart: any[] = [];
  productIDs: any[] = [];
  recent: any[] = [];
  userDetails: any[][] = [];
  size = 0;
  recentflag = false;
  loginflag = false;
  name = '';
  userId: number | null = null; // Store user ID for API calls

  constructor(private cartservice: CartService) { }

  addToCart(product: any) {
    this.cart.push(product);
    console.log(this.cart);
    this.productIDs.push(product.id);

    if (this.userId) {

      this.cartservice
        .addToCart(this.userId!, product.id)
        .subscribe((response) => {
          console.log('Added to cart:', response);
        });

    }
  }
  removefromCart(product: any) {
    this.cartservice.deleteFromCart(this.userId, product.id).subscribe((mess) => {
      console.log(mess);

    })
  }
  addToRecent(product: any) {
    this.recent.push(product);
    this.recentflag = true;
  }

  increament() {
    this.size++;
  }

  decreament() {
    if (this.size > 0) this.size--;
  }

  setLogin(user: any) {
    this.name = user.name;
    this.userDetails.push(user);
    console.log(this.userDetails);
    this.loginflag = true;
    this.userId = user.id; // Save user ID for later API calls
    console.log("productIDs", this.productIDs);
    for (let i = 0; i < this.size; i++) {
      this.cartservice
        .addToCart(this.userId!, this.productIDs[i])
        .subscribe((response) => {
          console.log('Added to cart:', response);
        });
    }


    this.cartservice.getCart(user.id).subscribe((value) => {
      console.log('Fetched cart items from server:', value);

      // Add the fetched items to the cart array
      this.cart.push(...value);

      // Update product IDs
      this.productIDs.push(...value.map((product: any) => product.id));

      // Update the size of the cart
      this.size = this.cart.length;

      console.log('Updated cart:', this.cart);
    });
  }

  // Call this when the service is destroyed

}
