import { Component, OnInit } from '@angular/core';
import { AdsService } from '../ads.service';
import { HttpEventType } from '@angular/common/http';
import { GlobalService } from '../global.service';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-postad',
  templateUrl: './postad.component.html',
  styleUrls: ['./postad.component.scss'],
})
export class PostadComponent  implements OnInit {
  isLoading:boolean=true;
  FeaturedAd:any=false;
  constructor(private adsService: AdsService, private global: GlobalService,private route:ActivatedRoute,private router:Router) {}
  adDetails: { 
    id: any; 
    title: string; 
    description: string; 
    price: string; 
    location: string; 
    phoneNo: string; 
    userId: any; 
  } = {
    id: null,  // Initialize id with null or any other appropriate value
    title: '',
    description: '',
    price: '',
    location: '',
    phoneNo: '',
    userId: this.global.userId,  // Example: Set the userId here
  };
  
userId : any = this.global.userId;
edit:boolean=false;
 
  ngOnInit(): void {

   this.checkFeatureAvailability(this.userId);
    this.route.queryParams.subscribe(params => {
      console.log('params',params);
      
      if (params['id']) {
        console.log('im in param');
        this.edit=true;
        // Assign values from query parameters to adDetails
        this.adDetails.id = params['id'];
        this.adDetails.title = params['title'];
        this.adDetails.description = params['description'];
        this.adDetails.price = params['price'];
        this.adDetails.phoneNo = params['phone'];
        this.adDetails.location = params['location'];
  
        // Handle images (image1 to image4)
        for (let i = 1; i <= 4; i++) {
          const imageParam = params[`image${i}`];
          if (imageParam) {
            this.uploadedImages.push({
              file:imageParam , // Query parameters won't have file objects
              preview: `http://localhost:8080/${imageParam}`, // Construct preview path
            });
          }
        }
      
      }
    });
  
    console.log('Ad Details:', this.adDetails);
    console.log('Uploaded Images:', this.uploadedImages);

    this.isLoading=false;
  }
  
 
  

  
  uploadedImages: { file: File; preview: string }[] = [];
  uploadProgress: number | null = null;


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

  submitAd(check:any): void {

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
    if (this.uploadedImages.length === 0 || !this.uploadedImages.some(image => image.file)) {
      alert('Please upload at least one image!');
      return;
    }
    const formData = new FormData();
    if(this.edit){
      formData.append('ad_id', this.adDetails.id);

    }
    if (check=='featured') {
      formData.append('isFeatured',this.FeaturedAd);
    }
    else{
      let flag:any=false;
      formData.append('isFeatured',flag);
    }
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
    if(!this.edit){
      this.adsService.postAdWithProgress(formData).subscribe({
        next: (event) => {
          if (event.type === HttpEventType.Response) {
            console.log('Ad posted successfully:', event.body);
            alert('Ad posted successfully!');
            setTimeout(() => this.router.navigate(['showads']), 1000); // Delay navigation
          }
        },
        error: (err) => {
          console.error('Failed to post ad:', err);
          alert('Failed to post ad. Please try again.');
        },
      });
      
      
    }
  else{

    this.adsService.editAdWithProgress(formData).subscribe({
      next: (event) => {
        if (event.type === HttpEventType.Response) {
          console.log('Ad edited successfully:', event.body);
          alert('Ad edited successfully!');
          this.resetForm();
          this.router.navigate(['showads']);
        }
      },
      error: (err) => {
        console.error('Failed to edit ad:', err);
        alert('Failed to edit ad. Please try again.');
      },
    });
    
  }
    
  }
  Delete(): void {
    if (confirm('Are you sure you want to delete this ad?')) {
      this.adsService.deleteAd(this.userId, this.adDetails.id).subscribe({
        next: (response: string) => {
          console.log('Ad deleted successfully:', response);
          this.resetForm();

          alert(response); // You can display the response message here
          this.router.navigate(['userads']);
        },
        error: (err) => {
          console.error('Error deleting ad:', err);
          alert(`Failed to delete ad. ${err.message || err.statusText}`);
        }
      });
    }
  }
  
  resetForm(): void {
    this.adDetails = {
      id:'',
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

  checkFeatureAvailability(userId: number): void {
    this.adsService.checkFeatureAvailability(userId).subscribe(
      (response) => {
        this.FeaturedAd= response; // Assign the result
        console.log('Feature availability:', this.FeaturedAd);
      },
      (error) => {
        console.error('Error checking feature availability:', error);
        this.FeaturedAd = false; // Handle error gracefully
      }
    );
  }
}
