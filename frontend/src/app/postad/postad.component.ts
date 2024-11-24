import { Component, OnInit } from '@angular/core';
import { AdsService } from '../ads.service';
import { HttpEventType } from '@angular/common/http';
import { GlobalService } from '../global.service';

@Component({
  selector: 'app-postad',
  templateUrl: './postad.component.html',
  styleUrls: ['./postad.component.scss'],
})
export class PostadComponent  implements OnInit {
  ngOnInit(): void {
   
  }
  

  adDetails = {
    title: 'abdullah',
    description: '',
    price: '',
    location: '',
    phoneNo: '',
    userId: this.global.userId, // Example: Set the userId here
  };
  userId : any = this.global.userId;

  uploadedImages: { file: File; preview: string }[] = [];
  uploadProgress: number | null = null;

  constructor(private adsService: AdsService, private global: GlobalService) {}

  triggerFileInput(imageId: string): void {
    const fileInput = document.getElementById(imageId) as HTMLInputElement;
    fileInput?.click();
  }

  onFileSelected(event: Event, index: number): void {
    const input = event.target as HTMLInputElement;
    if (input?.files && input.files[0]) {
      const file = input.files[0];
      const reader = new FileReader();

      reader.onload = () => {
        this.uploadedImages[index - 1] = {
          file,
          preview: reader.result as string,
        };
      };
      reader.readAsDataURL(file);
    }
  }

  submitAd(): void {
    if (
      !this.adDetails.title ||
      !this.adDetails.description ||
      !this.adDetails.price ||
      !this.adDetails.location ||
      !this.adDetails.phoneNo
    ) {
      alert('Please fill in all required fields!');
      return;
    }

    const formData = new FormData();
    formData.append('title', this.adDetails.title);
    formData.append('description', this.adDetails.description);
    formData.append('price', this.adDetails.price);
    formData.append('location', this.adDetails.location);
    formData.append('phoneNo', this.adDetails.phoneNo);
    formData.append('userId', this.userId);

    this.uploadedImages.forEach((imageObj, index) => {
      if (imageObj?.file) {
        formData.append(`image${index + 1}`, imageObj.file);
      }
    });

    this.adsService.postAdWithProgress(formData).subscribe({
      next: (event) => {
        if (event.type === HttpEventType.UploadProgress) {
          this.uploadProgress = Math.round(
            (100 * event.loaded) / (event.total || 1)
          );
          console.log(`Upload Progress: ${this.uploadProgress}%`);
        } else if (event.type === HttpEventType.Response) {
          console.log('Ad posted successfully:', event.body);
          alert('Ad posted successfully!');
          this.resetForm();
        }
      },
      error: (err) => {
        console.error('Failed to post ad:', err);
        alert('Failed to post ad. Please try again.');
      },
    });
  }

  resetForm(): void {
    this.adDetails = {
      title: '',
      description: '',
      price: '',
      location: '',
      phoneNo: '',
      userId: this.global.userId,
    };
    this.uploadedImages = [];
    this.uploadProgress = null;
  }
}
