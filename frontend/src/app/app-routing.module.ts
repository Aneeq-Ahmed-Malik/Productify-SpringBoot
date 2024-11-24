import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { ViewallComponent } from './viewall/viewall.component';
import { PropertyRead } from '@angular/compiler';
import { ProductDetailsComponent } from './product-details/product-details.component';
import { CartComponent } from './cart/cart.component';
import { ShowAdsComponent } from './show-ads/show-ads.component';
import { AdsdetailsComponent } from './adsdetails/adsdetails.component';
import { PostadComponent } from './postad/postad.component';
import { LoginComponent } from './login/login.component';
import { TestComponent } from './test/test.component';
import { UseradsComponent } from './userads/userads.component';
const routes: Routes = [
  { path: '', redirectTo: 'home', pathMatch: 'full' },
  { path: 'home', component: HomeComponent },
  {path:'viewall',component:ViewallComponent},
  {path:'productdetails',component:ProductDetailsComponent},
  {path:'cart',component:CartComponent},
  {path:'showads',component:ShowAdsComponent},
  {path:'adsdetails',component:AdsdetailsComponent},
  {path:'postad',component:PostadComponent},
  {path:'login',component:LoginComponent},
  {path:'test',component:TestComponent},
  {path:'userads',component:UseradsComponent}
  
  
  
];

@NgModule({
  
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
