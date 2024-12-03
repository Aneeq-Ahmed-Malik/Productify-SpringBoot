import { Component, OnInit } from '@angular/core';
import { GlobalService } from '../global.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent implements OnInit {
  searchTerm: string = ''; // Holds the value of the search input
  categories = [
    { name: 'Gaming Headsets', websites: ['Amazon', 'AliExpress', 'Newgg'] },
    { name: 'Playstation 5 Controllers', websites: ['Amazon', 'AliExpress', 'Newgg'] },
    { name: 'Gaming Keyboards', websites: ['Amazon', 'AliExpress', 'Newgg'] },
    { name: 'Gaming Mice', websites: ['Amazon', 'AliExpress', 'Newgg'] },
    { name: 'Nintendo Switch Accessories', websites: ['Amazon', 'AliExpress', 'Newgg'] },
    { name: 'Playstation 5 Games', websites: ['Amazon', 'AliExpress', 'Newgg'] },
    { name: 'Gaming Monitors', websites: ['Amazon', 'AliExpress', 'Newgg'] },
    { name: 'Virtual Reality Accessories', websites: ['Amazon', 'AliExpress', 'Newgg'] },
    { name: 'Gaming Chairs', websites: ['Amazon', 'AliExpress', 'Newgg'] },
    { name: 'Playstation 4 Accessories', websites: ['Amazon', 'AliExpress', 'Newgg'] }
  ];

  constructor(protected global: GlobalService, private router: Router) {}

  ngOnInit() {}

  // Search handler
  onSearch(): void {
    console.log('Search Term:', this.searchTerm);
    this.router.navigate(['viewall'], { queryParams: { search: this.searchTerm } });

    // You can now call an API or perform other actions with the search term
  }

  // Voice search handler
  startVoiceSearch(): void {
    if ('webkitSpeechRecognition' in window) {
      const recognition = new (window as any).webkitSpeechRecognition();
      recognition.lang = 'en-US'; // Language for voice recognition
      recognition.continuous = false; // Stop after one input
      recognition.interimResults = false; // Final results only

      recognition.start();

      recognition.onresult = (event: any) => {
        const transcript = event.results[0][0].transcript; // Get the recognized text
        this.searchTerm = transcript; // Set the recognized text as the search term
        console.log('Voice Input:', transcript);
        this.onSearch(); // Trigger search after voice input
      };

      recognition.onerror = (event: any) => {
        console.error('Voice recognition error:', event.error);
      };
    } else {
      alert('Voice search is not supported on this browser.');
    }
  }

  viewAll(category: string, website: string): void {
    this.router.navigate(['viewall'], { queryParams: { category: category, website: website } });
    console.log('Category:', category);
    console.log('Website:', website);
  }

  Routing(route: string): void {
    this.router.navigate([route]);
  }
}
