import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-adsdetails',
  templateUrl: './adsdetails.component.html',
  styleUrl: './adsdetails.component.scss'
})
export class AdsdetailsComponent  implements OnInit {
  constructor(private route : ActivatedRoute){}
  isLoading:boolean=true
  product:any;
  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      if (params) {
        // Assign values from query parameters to product object
        this.product = {
          id: params['id'],
          title: params['title'],
          description: params['description'],
          price: params['price'],
          image1: params['image1'],
          image2: params['image2'],
          image3: params['image3'],
          image4: params['image4'],
          phone:  params['phone'] ,
          location: params['location'],
          name:params['name']
        };
      }
    });

    console.log(this.product);
    setTimeout(() => {
      this.isLoading=false;
    }, 6000);
    
  }

  openWhatsApp( message: string): void {
    // Encode the message to ensure special characters are handled
    const encodedMessage = encodeURIComponent(message);

    // Construct the WhatsApp URL
    const whatsappUrl = `https://wa.me/${this.product.phone}?text=${encodedMessage}`;

    // Open WhatsApp in a new tab
    window.open(whatsappUrl, '_blank');
  }
 
}
