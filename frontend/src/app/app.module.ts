import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { ViewallComponent } from './viewall/viewall.component';
import { ProductDetailsComponent } from './product-details/product-details.component';
import { CartComponent } from './cart/cart.component';
import { ShowAdsComponent } from './show-ads/show-ads.component';
import { AdsdetailsComponent } from './adsdetails/adsdetails.component';
import { PostadComponent } from './postad/postad.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    HeaderComponent,
    FooterComponent,
    ViewallComponent,
    ProductDetailsComponent,
    CartComponent,
    ShowAdsComponent,
    AdsdetailsComponent,
    PostadComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
