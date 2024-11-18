import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.scss'] // Typo corrected from 'styleUrl' to 'styleUrls'
})
export class ProductDetailsComponent implements OnInit {
  selectedItemIndex: number=0;
  product:any={};
    
  products = [
    { id: 1, image: '../../assets/img/what1.jpg', title: 'PlayStation 5' },
    { id: 2, image: '../../assets/img/Gaming Chairs965.1.png', title: 'Xbox Series X' },
    { id: 3, image: '../../assets/img/what1.jpg', title: 'Nintendo Switch' },
    { id: 4, image: '../../assets/img/Gaming Chairs965.1.png', title: 'Gaming Chair' }
  ];

  constructor(private route: ActivatedRoute) {}
  path="../../assets/img/what1.jpg";
  ngOnInit() {
    // Subscribe to query parameters
    this.route.queryParams.subscribe(params => {
      if (params) {
        // Assign values from query parameters to product object
        this.product.id = params['id'];
        this.product.title = params['title'];
        this.product.price = params['price'];
        console.log('Product:', this.product);
      } else {
        console.log("No product data found in query parameters");
      }
    });
  }

  selectItem(index: number,path:string) {
    this.selectedItemIndex = index;
    this.path=path;
  }
  
}
