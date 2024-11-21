import { Component } from '@angular/core';
import { GlobalService } from '../global.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {
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
}
