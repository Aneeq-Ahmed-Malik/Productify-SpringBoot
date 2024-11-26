import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { GlobalService } from '../global.service';
import { DataService } from '../data.service';
@Component({
  selector: 'app-product-details',
  templateUrl: './product-details.component.html',
  styleUrls: ['./product-details.component.scss'] // Typo corrected from 'styleUrl' to 'styleUrls'
})
export class ProductDetailsComponent implements OnInit {

  selectedItemIndex: number=0;
  product:any={};
  path:any=null;
  loading: boolean = true; // Loading flag
  sentencePairs: string[] = [];
  recentproducts :any[][]=[];
  recommendproducts :any[][]=[];
  loadingRecommendations :boolean=true;
  
  fullStars: number = 0; // Full stars
  halfStar: boolean = false; // Half star flag
  emptyStars: number = 0; // Empty stars
  constructor(private route: ActivatedRoute,protected global:GlobalService,private data:DataService,private router:Router) {}

  ngOnInit() {
    setTimeout(() => {
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

          // Calculate rating
          this.fullStars = Math.floor(this.product.rating);
          this.halfStar = this.product.rating % 1 >= 0.5;
          this.emptyStars = 5 - this.fullStars - (this.halfStar ? 1 : 0);

          this.path = '../../assets/' + this.product.image1.replace('./', '');
          console.log('Product:', this.product);
        } else {
          console.log("No product data found in query parameters");
        }
      });

      this.processDescription();
      this.recentproducts = this.chunkArray(this.global.recent, 3);
      this.loading = false;
      this.getRecommendation();

    }, 2000);
  }


getRecommendation(){
  const id = [this.product.id];
  console.log("recomd id",id) ;

  this.data.getRecommendations(id).subscribe(
    (data: any[]) => {
        this.recommendproducts = this.chunkArray(data, 4);
        console.log('Recommendations:', this.recommendproducts);
        this.loadingRecommendations = false;
    },
    (error: any) => {
        console.error('Error fetching recommendations:', error);
        this.loadingRecommendations = false;
    }
);
}

Routing(product:any) {
  this.loading=true;
  this.global.addToRecent(product);
  this.recentproducts=this.chunkArray(this.global.recent,3);
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
  
  setTimeout(() => {
    this.loading=false;
    
  }, 2000);
  
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
  chunkArray(array: any[], size: number): any[][] {
    const chunks: any[][] = [];
    for (let i = 0; i < array.length; i += size) {
      chunks.push(array.slice(i, i + size));
    }
    return chunks;
  }
}
