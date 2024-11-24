import { Component } from '@angular/core';
import { GlobalService } from '../global.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {
 
  searchTerm: string = '';
  categories = [
    { 
      name: 'Gaming Headsets', 
      websites: ['Amazon', 'Ali Express', 'Newgg'] 
    },
    { 
      name: 'Playstation 5 Controllers', 
      websites: ['Amazon', 'Ali Express', 'Newgg'] 
    },
    { 
      name: 'Gaming Keyboards', 
      websites: ['Amazon', 'Ali Express', 'Newgg'] 
    },
    { 
      name: 'Gaming Mice', 
      websites: ['Amazon', 'Ali Express', 'Newgg'] 
    },
    { 
      name: 'Nintendo Switch Accessories', 
      websites: ['Amazon', 'Ali Express', 'Newgg'] 
    },
    { 
      name: 'Playstation 5 Games', 
      websites: ['Amazon', 'Ali Express', 'Newgg'] 
    },
    { 
      name: 'Gaming Monitors', 
      websites: ['Amazon', 'Ali Express', 'Newgg'] 
    },
    { 
      name: 'Virtual Reality Accessories', 
      websites: ['Amazon', 'Ali Express', 'Newgg'] 
    },
    { 
      name: 'Gaming Chairs', 
      websites: ['Amazon', 'Ali Express', 'Newgg'] 
    },
    { 
      name: 'Playstation 4 Accessories', 
      websites: ['Amazon', 'Ali Express', 'Newgg'] 
    }
  ];
constructor(protected global:GlobalService,private router:Router){}
viewAll(category:string,website:string){
  this.router.navigate(['viewall'],{queryParams:{category:category,website:website}});
  console.log("Category",category);
  console.log("website",website);
  
  
}
 // Holds the value of the search input

  onSearch(): void {
    console.log('Search Term:', this.searchTerm);
    this.router.navigate(['viewall'],{queryParams:{search:this.searchTerm}});

    // You can now call an API or perform other actions with the search term
    // Example: this.searchService.searchProducts(this.searchTerm).subscribe(...)
  }
Routing(route:string){
  this.router.navigate([route]);
}
}
