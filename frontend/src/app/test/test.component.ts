import { Component, AfterViewInit } from '@angular/core';
import { AdsService } from '../ads.service';
import { HttpEventType } from '@angular/common/http';
declare var bootstrap: any;
@Component({
  selector: 'app-test',
  templateUrl: './test.component.html',
  styleUrl: './test.component.scss'
})
export class TestComponent {
  title: string = '';
  description: string = '';
  selectedFiles: File[] = [];
  uploadProgress:any;

  constructor(private fileUploadService: AdsService) {}

  onFileSelected(event: any) {
    if (event.target.files) {
      this.selectedFiles = Array.from(event.target.files);
    }
  }

  onSubmit() {
    const formData = new FormData();
    formData.append('title', this.title);
    formData.append('description', this.description);
  
    // Append selected files as an array of files (key = "files")
    this.selectedFiles.forEach((file) => {
      formData.append('files', file);
    });
  
    // Call the uploadMultipartData method
    this.fileUploadService.uploadMultipartData(formData).subscribe({
      next: (event) => {
        if (event.type === HttpEventType.UploadProgress) {
          this.uploadProgress = Math.round((100 * event.loaded) / (event.total || 1));
          console.log(`Upload Progress: ${this.uploadProgress}%`);
        } else if (event.type === HttpEventType.Response) {
          console.log('Upload complete:', event.body);
          alert('Upload successful!');
        }
      },
      error: (err) => {
        console.error('Error uploading files:', err);
      },
    });
  }
  
}
