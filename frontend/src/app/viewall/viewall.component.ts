import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { DataService } from '../data.service';
import { GlobalService } from '../global.service';
@Component({
  selector: 'app-viewall',
  templateUrl: './viewall.component.html',
  styleUrls: ['./viewall.component.scss'], // Corrected typo
})
export class ViewallComponent implements OnInit {
  loading = true; // Initially set loading to true
  website: any = null;
  category: any = null;
  products: any[][] = [];
  currentSlide = 0; // State for carousel
  recentproducts :any[][]=[];
  recommended:any[][]=[];
  constructor(private router: Router, private data: DataService, private route: ActivatedRoute,protected global:GlobalService) {}

  ngOnInit() {
    // Subscribe to query parameters
    this.route.queryParams.subscribe((params) => {
      if (params) {
        this.category = params['category'];
        this.website = params['website'];

        if (this.category && this.website) {
          // Fetch products by category and website
          this.fetchProductsByCategoryAndWebsite(this.category, this.website);
          this.Recommended(this.global.cartIDs);
          this.recentproducts=this.chunkArray(this.global.recent,3);
          console.log("this is recent in viewall",this.recentproducts);
        
        } else if (this.category) {
          // Fetch products by category only
          this.fetchProductsByCategory(this.category);
          this.Recommended(this.global.cartIDs);
          this.recentproducts=this.chunkArray(this.global.recent,4);
          

          console.log("this is recent in viewall",this.recentproducts);
          
        } else {
          console.log('No valid parameters provided.');
          this.loading = false; // Stop loading if no valid parameters
        }
      }
    });
  }
  Recommended(productIds:any){
    this.data.getRecommendations(productIds).subscribe(
      (data: any[]) => {
        this.recommended=this.chunkArray(data,4);         // Stop loading animation
        console.log('Recommendations:', this.recommended);
        this.loading=false;
      },
      (error: any) => {
        console.error('Error fetching recommendations:', error);
        this.loading=false;
      }
    );
    
  }
  // Fetch products by category and website
  private fetchProductsByCategoryAndWebsite(category: string, website: string) {
    this.data.getProductsByCatWeb(category, website).subscribe(
      (values: any[]) => {
        this.products = this.chunkArray(values, 12); // Chunk products into slides
        console.log('Fetched products:', this.products);
        this.loading = false; // Stop loading once data is fetched
      },
      (error) => {
        console.error('Error fetching products:', error);
        this.loading = false; // Stop loading in case of an error
      }
    );
  }

  // Fetch products by category only
  private fetchProductsByCategory(category: string) {
    this.data.getProductsByCategory(category).subscribe(
      (values: any[]) => {
        this.products = this.chunkArray(values, 12); // Chunk products into slides
        console.log('Fetched products:', this.products);
        this.loading = false; // Stop loading once data is fetched
       

      },
      (error) => {
        console.error('Error fetching products:', error);
        this.loading = false; // Stop loading in case of an error
      }
    );
  }

  // Method to move to the next slide
  nextSlide() {
    if (this.currentSlide < this.products.length - 1) {
      this.currentSlide++;
    }
  }

  // Method to move to the previous slide
  previousSlide() {
    if (this.currentSlide > 0) {
      this.currentSlide--;
    }
  }

 

  chunkArray(array: any[], size: number): any[][] {
    const chunks: any[][] = [];
    for (let i = 0; i < array.length; i += size) {
      chunks.push(array.slice(i, i + size));
    }
    return chunks;
  }
  Routing(product:any) {
    
    console.log('before route',product);
    this.router.navigate(['productdetails'], {
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
  addToCart(product:any){
    this.global.addToCart(product);
    this.global.increament();
    this.Recommended(this.global.cartIDs);
    this.loading=true;
  }
}
