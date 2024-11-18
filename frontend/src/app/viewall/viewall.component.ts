import { Component } from '@angular/core';
import { Router,ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-viewall',
  templateUrl: './viewall.component.html',
  styleUrl: './viewall.component.scss'
})
export class ViewallComponent {
  constructor(private router:Router){}

  slides = [
    // Example slide data for the first carousel
    { items: Array(12).fill({ image: '../../assets/img/what1.jpg', title: 'Item' }) },
    { items: Array(12).fill({ image: '../../assets/img/Gaming Chairs965.1.png', title: 'Item' }) }
  ];

  currentSlide = 0; // State for first carousel

  // Method to move to the next slide for the first carousel
  nextSlide() {
    if (this.currentSlide < this.slides.length - 1) {
      this.currentSlide++;
      this.updateActiveSlide('carouselExample');
    }
  }

  // Method to move to the previous slide for the first carousel
  previousSlide() {
    if (this.currentSlide > 0) {
      this.currentSlide--;
      this.updateActiveSlide('carouselExample');
    }
  }

  // Update the active slide manually for a specific carousel
  updateActiveSlide(carouselId: string) {
    const carouselElement = document.getElementById(carouselId);
    if (!carouselElement) return;

    const carouselItems = carouselElement.querySelectorAll('.carousel-item');
    carouselItems.forEach((item, index) => {
      if (index === this.currentSlide) {
        item.classList.add('active');
      } else {
        item.classList.remove('active');
      }
    });
  }
  products = [
    { id: 1, image: '../../assets/img/what1.jpg', title: 'PlayStation 5', price: 499.99 },
    { id: 2, image: '../../assets/img/Gaming Chairs965.1.png', title: 'Xbox Series X', price: 499.99 }
  ];

  goToDetails(product: any) {
    // Navigate with query parameters
    this.router.navigate(['/productdetails'], { queryParams: { id: product.id, title: product.title, price: product.price } });
  }
}
