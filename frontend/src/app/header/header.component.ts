import { Component, OnInit } from '@angular/core';
import { GlobalService } from '../global.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent implements OnInit {
  ngOnInit(){
  }
  searchTerm: string = '';
  categories = [
    { 
      name: 'Gaming Headsets', 
      websites: ['Amazon', 'AliExpress', 'Newgg'] 
    },
    { 
      name: 'Playstation 5 Controllers', 
      websites: ['Amazon', 'AliExpress', 'Newgg'] 
    },
    { 
      name: 'Gaming Keyboards', 
      websites: ['Amazon', 'AliExpress', 'Newgg'] 
    },
    { 
      name: 'Gaming Mice', 
      websites: ['Amazon', 'AliExpress', 'Newgg'] 
    },
    { 
      name: 'Nintendo Switch Accessories', 
      websites: ['Amazon', 'AliExpress', 'Newgg'] 
    },
    { 
      name: 'Playstation 5 Games', 
      websites: ['Amazon', 'AliExpress', 'Newgg'] 
    },
    { 
      name: 'Gaming Monitors', 
      websites: ['Amazon', 'AliExpress', 'Newgg'] 
    },
    { 
      name: 'Virtual Reality Accessories', 
      websites: ['Amazon', 'AliExpress', 'Newgg'] 
    },
    { 
      name: 'Gaming Chairs', 
      websites: ['Amazon', 'AliExpress', 'Newgg'] 
    },
    { 
      name: 'Playstation 4 Accessories', 
      websites: ['Amazon', 'AliExpress', 'Newgg'] 
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
