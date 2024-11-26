import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { DataService } from '../data.service';
import { forkJoin } from 'rxjs';
import { GlobalService } from '../global.service';
@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  images: string[] = [
    '../../assets/img/img1.jpg',
    '../../assets/img/img2.jpg',
    '../../assets/img/img3.jpg'
  ];
  // Array of category configurations
  categories = [
    { name: 'Gaming Keyboards', chunkSize: 4, key: 'keyboards' },
    { name: 'Gaming Chairs', chunkSize: 3, key: 'chairs' },
    { name: 'Playstation 5 Games', chunkSize: 3, key: 'playstation5' },
    { name: 'Gaming Mice', chunkSize: 4, key: 'mice' },
    { name: 'Nintendo Switch Accessories', chunkSize: 3, key: 'Nintendo' },
    { name: 'Gaming Monitors', chunkSize: 4, key: 'monitor' },
    { name: 'Virtual Reality Accessories', chunkSize: 3, key: 'vr' },
    { name: 'Playstation 5 Controllers', chunkSize: 4, key: 'controller' },
    { name: 'Playstation 4 Accessories', chunkSize: 4, key: 'playstation4' },
    { name: 'Gaming Headsets', chunkSize: 3, key: 'headsets' }
  ];
  loading: boolean = true; // Loading flag

  // Separate arrays for each category
  keyboards: any[][] = [];
  chairs: any[][] = [];
  playstation5: any[][] = [];
  mice: any[][] = [];
  Nintendo: any[][] = [];
  monitor: any[][] = [];
  vr: any[][] = [];
  controller: any[][] = [];
  playstation4: any[][] = [];
  headsets: any[][] = [];

  errorMessage: string = '';

  constructor(private route: Router, private productService: DataService,private global:GlobalService) {}
  
  ngOnInit() {
    // this.initAutoScroll();
    // Create an array of observables for each category
    const observables = this.categories.map(category => 
      this.productService.getProductsByCategory(category.name)
    );
    forkJoin(observables).subscribe(
      (results) => {
        // Process the results for each category
        results.forEach((categoryData, index) => {
          const category = this.categories[index];
          (this as any)[category.key] = this.chunkArray(categoryData, category.chunkSize);
          console.log(`Grouped ${category.name}:`, (this as any)[category.key]);
        });
    
        this.loading = false; // Stop loading when all data is ready
      },
      (error) => {
        this.errorMessage = `Error fetching products`;
        console.error(error);
        this.loading = false; // Stop loading even if there's an error
      }
    );
    


  }
  

  Routing(product:any,category:any=null) {
    if(product=='viewwall'){
      this.route.navigate(['viewall'],{queryParams:{category:category}});
      console.log("Category",category);
    

    }
    console.log('before route',product);
    this.route.navigate(['productdetails'], {
      queryParams: {
        id: product.id,
        title: product.title,
        description: product.description,
        link: product.link,
        rating: product.rating,
        price: product.price,
        image1: product.image1,
        image2: product.image2,
        image3: product.image3,
        image4: product.image4,
        categoryId: product.category.id, // Nested attribute
        websiteId: product.website.id // Nested attribute
      }
    });
    this.global.addToRecent(product);
  }

  chunkArray(array: any[], size: number): any[][] {
    const chunks: any[][] = [];
    for (let i = 0; i < array.length; i += size) {
      chunks.push(array.slice(i, i + size));
    }
    return chunks;
  }
  addToCart(product:any){
    this.global.addToCart(product);
    this.global.increament();
  }
  
}
