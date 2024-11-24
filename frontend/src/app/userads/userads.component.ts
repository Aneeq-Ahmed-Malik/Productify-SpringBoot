import { Component, OnInit } from '@angular/core';
import { GlobalService } from '../global.service';
import { AdsService } from '../ads.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-userads',
  templateUrl: './userads.component.html',
  styleUrl: './userads.component.scss'
})
export class UseradsComponent implements OnInit {
  constructor(private global:GlobalService,private adService:AdsService,private router:Router){}
  ads:any;
  ngOnInit(): void {
   this.fetchAds();
  }
  fetchAds(): void {
    this.adService.getAdsByUserId(this.global.userId).subscribe({
      next: (data) => {
        this.ads = data;
        console.log('Fetched ads:', this.ads);
      },
      error: (err) => {
        console.error('Error fetching ads:', err);
      },
    });
  }
  Routing(ad: any) {
    this.router.navigate(['postad'], {
      queryParams: {
        id: ad.id,
        title: ad.title,
        description: ad.description,
        price: ad.price,
        image1: ad.image1,
        image2: ad.image2,
        image3: ad.image3,
        image4: ad.image4,
        phone: ad.phoneNo,
        location:ad.location,
        name:ad.user.name
      }
    });
  }
  getFirstAvailableImage(product: any): string | null {
    // Check for image1 first, if not available then check image2, then image3, and so on
    if (product.image1) {
      console.log(product.image1);
      
      return product.image1;
    } else if (product.image2) {
      console.log(product.image2);

      return product.image2;
    } else if (product.image3) {
      return product.image3;
    } else if (product.image4) {
      return product.image4;
    }
    return null; // No image available
  }
}
