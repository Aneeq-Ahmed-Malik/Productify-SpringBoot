import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-adsdetails',
  templateUrl: './adsdetails.component.html',
  styleUrl: './adsdetails.component.scss'
})
export class AdsdetailsComponent  implements OnInit {
  ngOnInit(): void {
   
  }
  images: string[] = [
    'https://static-01.daraz.pk/p/37cd9cf9ea23a97b7d3097bfd9a03347.jpg_720x720.jpg_.webp',
    'https://via.placeholder.com/720x720.png?text=Image+2',
    'https://via.placeholder.com/720x720.png?text=Image+3',
    'https://via.placeholder.com/720x720.png?text=Image+4',
  ];
}
