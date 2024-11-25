import { Component, OnInit } from '@angular/core';
import { AdsService } from '../ads.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-show-ads',
  templateUrl: './show-ads.component.html',
  styleUrls: ['./show-ads.component.scss']
})
export class ShowAdsComponent implements OnInit {
  ads: any = [];
  isLoading: boolean = true;
  timestamp: number = new Date().getTime(); // Generate unique timestamp

  constructor(private adservice: AdsService, private router: Router) {}

  ngOnInit(): void {
    this.fetchAllAds();
  }

  fetchAllAds() {
    this.isLoading = true; // Show loading spinner
    this.adservice.getAllAds().subscribe(
      (data) => {
        this.ads = data;
        this.timestamp = new Date().getTime(); // Update timestamp after fetching ads
        this.isLoading = false; // Hide loading spinner
      },
      (error) => {
        console.error('Error fetching ads:', error);
        this.isLoading = false; // Hide loading spinner even if the API fails
      }
    );
  }

  Routing(ad: any) {
    this.router.navigate(['addetails'], {
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
        location: ad.location,
        name: ad.user.name
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
