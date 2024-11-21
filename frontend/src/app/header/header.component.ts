import { Component } from '@angular/core';
import { GlobalService } from '../global.service';
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {
  categories = [
    { 
      name: 'Gaming Headsets', 
      websites: ['Amazon', 'Ali Express', 'Newegg'] 
    },
    { 
      name: 'Playstation 5 Controllers', 
      websites: ['Amazon', 'Ali Express', 'Newegg'] 
    },
    { 
      name: 'Gaming Keyboards', 
      websites: ['Amazon', 'Ali Express', 'Newegg'] 
    },
    { 
      name: 'Gaming Mice', 
      websites: ['Amazon', 'Ali Express', 'Newegg'] 
    },
    { 
      name: 'Nintendo Switch Accessories', 
      websites: ['Amazon', 'Ali Express', 'Newegg'] 
    },
    { 
      name: 'Playstation 5 Games', 
      websites: ['Amazon', 'Ali Express', 'Newegg'] 
    },
    { 
      name: 'Gaming Monitors', 
      websites: ['Amazon', 'Ali Express', 'Newegg'] 
    },
    { 
      name: 'Virtual Reality Accessories', 
      websites: ['Amazon', 'Ali Express', 'Newegg'] 
    },
    { 
      name: 'Gaming Chairs', 
      websites: ['Amazon', 'Ali Express', 'Newegg'] 
    },
    { 
      name: 'Playstation 4 Accessories', 
      websites: ['Amazon', 'Ali Express', 'Newegg'] 
    }
  ];
constructor(protected global:GlobalService){}

}
