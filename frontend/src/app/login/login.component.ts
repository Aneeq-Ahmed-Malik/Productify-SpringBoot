import { Component } from '@angular/core';
import { AuthService } from '../auth.service';
import { Router } from '@angular/router';
import { GlobalService } from '../global.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent {
  activeTab: string = 'Login'; // Default tab
  loginError: string = ''; // Error message for login
  signupError: string = ''; // Error message for signup
  showLoginPassword: boolean = false; // Toggle for login password visibility
  showSignupPassword: boolean = false; // Toggle for signup password visibility

  loginData = {
    email: '',
    password: '',
  };

  signupData = {
    name: '',
    email: '',
    password: '',
  };

  constructor(private authService: AuthService,private router :Router,private global:GlobalService) {}

  setActiveTab(tabName: string): void {
    this.activeTab = tabName;
    this.loginError = ''; // Clear login error on tab switch
    this.signupError = ''; // Clear signup error on tab switch
  }

  onInputChange(event: Event, formType: string, field: string): void {
    const input = event.target as HTMLInputElement;
    if (formType === 'login') {
      this.loginData[field as keyof typeof this.loginData] = input.value;
    } else if (formType === 'signup') {
      this.signupData[field as keyof typeof this.signupData] = input.value;
    }
  }
  

  onLogin(): void {
    console.log(this.loginData);
    
    if (!this.loginData.email || !this.loginData.password) {
      this.loginError = 'Please fill in all required fields.';
      return;
    }

    this.authService.login(this.loginData).subscribe(
      (response) => {
        if (response) {
          console.log(response);
          
          alert('Login successful!');
          this.global.setLogin(response);
          this.router.navigate(['home'])

        } else {
          console.log(response);
          
          this.loginError = 'Invalid email or password.';
        }
      },
      (error) => {
        this.loginError = 'Login failed. Please try again later.';
        console.error('Login failed:', error);
      }
    );
  }

  onSignup(): void {
    if (
      !this.signupData.name ||
      !this.signupData.email ||
      !this.signupData.password
    ) {
      this.signupError = 'Please fill in all required fields.';
      return;
    }
    console.log(this.signupData);
    
    this.authService.signup(this.signupData).subscribe(
      (response) => {
        console.log(response);
        

        this.setActiveTab('Login');
        alert('Signup successful!Now Login Plzz');

      },
      (error) => {
        this.signupError = 'Signup failed. Please try again later.';
        console.error('Signup failed:', error);
      }
    );
  }
}
