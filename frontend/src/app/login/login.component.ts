import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  loginData = {
    email: '',
    password: '',
  };

  signupData = {
    name: '',
    email: '',
    phone: '',
    password: '',
  };

  constructor(private authService: AuthService) {}
  activeTab: string = 'Login'; // Default tab

  // Method to set active tab
  setActiveTab(tabName: string): void {
    this.activeTab = tabName;
  }
  onLogin() {
    this.authService.login(this.loginData).subscribe(
      (response) => {
        console.log('Login successful:', response);
        alert('Login successful!');
      },
      (error) => {
        console.error('Login failed:', error);
        alert('Login failed!');
      }
    );
  }

  onSignup() {
    this.authService.signup(this.signupData).subscribe(
      (response) => {
        console.log('Signup successful:', response);
        alert('Signup successful!');
      },
      (error) => {
        console.error('Signup failed:', error);
        alert('Signup failed!');
      }
    );
  }

}
