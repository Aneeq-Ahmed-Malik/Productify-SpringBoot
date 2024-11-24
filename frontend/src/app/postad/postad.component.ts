import { Component } from '@angular/core';
import { AdsService } from '../ads.service';

@Component({
  selector: 'app-postad',
  templateUrl: './postad.component.html',
  styleUrls: ['./postad.component.scss'],
})
export class PostadComponent {
  adDetails = {
    title: '',
    description: '',
    price: '',
    location: '',
    name: '',
    phoneNo: '',
  };

  uploadedImages: File[] = []; // Array to hold uploaded files

  constructor(private adsService: AdsService) {}

  triggerFileInput(imageId: string): void {
    const fileInput = document.getElementById(imageId) as HTMLInputElement;
    fileInput?.click();
  }

  onFileSelected(event: Event, index: number): void {
    const input = event.target as HTMLInputElement;
    if (input?.files && input.files[0]) {
      this.uploadedImages[index - 1] = input.files[0]; // Store file in the correct index
    }
  }

  downloadImages(): void {
    if (
      !this.adDetails.title ||
      !this.adDetails.description ||
      !this.adDetails.price ||
      !this.adDetails.location ||
      !this.adDetails.name ||
      !this.adDetails.phoneNo
    ) {
      alert('Please fill in all required fields!');
      return;
    }

    const formData = new FormData();
    formData.append('ad', JSON.stringify(this.adDetails)); // Add ad details as JSON

    // Append images to formData
    this.uploadedImages.forEach((file, index) => {
      if (file) {
        formData.append(`image${index + 1}`, file); // Name each image as image1, image2, etc.
      }
    });

    this.adsService.postAd(formData).subscribe(
      (response:any) => {
        console.log('Ad posted successfully:', response);
        alert('Ad posted successfully!');
      },
      (error:any) => {
        console.error('Failed to post ad:', error);
        alert('Failed to post ad. Please try again.');
      }
    );
  }
}
