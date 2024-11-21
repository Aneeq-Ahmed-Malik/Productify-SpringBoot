import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { GlobalService } from '../global.service';
@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.scss'] // Typo corrected from 'styleUrl' to 'styleUrls'
})
export class ProductDetailsComponent implements OnInit {

  selectedItemIndex: number=0;
  product:any={};
  path="../../assets/img/what1.jpg";
  loading: boolean = true; // Loading flag
  sentencePairs: string[] = [];
  products = [
    { id: 1, image: '../../assets/img/what1.jpg', title: 'PlayStation 5' },
    { id: 2, image: '../../assets/img/Gaming Chairs965.1.png', title: 'Xbox Series X' },
    { id: 3, image: '../../assets/img/what1.jpg', title: 'Nintendo Switch' },
    { id: 4, image: '../../assets/img/Gaming Chairs965.1.png', title: 'Gaming Chair' }
  ];

  constructor(private route: ActivatedRoute,private global:GlobalService) {}

  ngOnInit() {
    // Subscribe to query parameters
    setTimeout(() =>{

      this.route.queryParams.subscribe(params => {
        if (params) {
          // Assign values from query parameters to product object
          this.product = {
            id: params['id'],
            title: params['title'],
            description: params['description'],
            link: params['link'],
            rating: params['rating'],
            price: params['price'],
            image1: params['image1'],
            image2: params['image2'],
            image3: params['image3'],
            image4: params['image4'],
            category: { id: params['categoryId'] },
            website: { id: params['websiteId'] }
          };
          this.path='../../assets/' + this.product.image1.replace('./', '');
          console.log('Product:', this.product);
        } else {
          console.log("No product data found in query parameters");
        }
      });
      this.processDescription();
      this.loading = false; // Stop loading when data is ready

    },2000);
    
  }

  selectItem(index: number,path:string) {
    this.selectedItemIndex = index;
    this.path='../../assets/' + path.replace('./', '');
  }
  redirectTo(link: string) {
    //window.location.href = link; // Opens the link in the same tab
    window.open(link, '_blank'); // Opens the link in a new tab

  }
  processDescription() {
    if (this.product.description) {
      // Split description into sentences
      const sentences = this.product.description.split('.').filter((s:any) => s.trim().length > 0);

      // Group sentences into pairs
      for (let i = 0; i < sentences.length; i += 2) {
        const pair = sentences[i] + (sentences[i + 1] ? '. ' + sentences[i + 1] : '');
        this.sentencePairs.push(pair.trim());
      }
    }
  }
  addToCart(product:any){
    this.global.addToCart(product);
    this.global.increament();
  }
}
