import { Component } from '@angular/core';
import { GlobalService } from '../global.service';
@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent {
  
constructor(protected global:GlobalService){}

}
