import { Component } from '@angular/core';

@Component({
  selector: 'app-postad',
  templateUrl: './postad.component.html',
  styleUrl: './postad.component.scss'
})
export class PostadComponent {
  uploadedImages: { name: string; url: string }[] = [];

  // Trigger file input by ID
  triggerFileInput(inputId: string) {
    const fileInput = document.getElementById(inputId) as HTMLInputElement;
    if (fileInput) {
      fileInput.click();
    }
  }

  // Handle file selection
  onFileSelected(event: Event, index: number) {
    const fileInput = event.target as HTMLInputElement;
    if (fileInput.files && fileInput.files.length > 0) {
      const file = fileInput.files[0];
      const reader = new FileReader();
      reader.onload = () => {
        this.uploadedImages[index - 1] = {
          name: file.name,
          url: reader.result as string
        };
      };
      reader.readAsDataURL(file);
    }
  }

  // Trigger download of uploaded images
  downloadImages() {
    this.uploadedImages.forEach(image => {
      const link = document.createElement('a');
      link.href = image.url;
      link.download = `MyAdsFolder/${image.name}`;
      link.click();
    });
  }


}
