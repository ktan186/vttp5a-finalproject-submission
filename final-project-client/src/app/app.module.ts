import { NgModule, isDevMode } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { GoogleMapsModule } from '@angular/google-maps';

import { AppComponent } from './app.component';
import { MapComponent } from './components/map.component';
import { MaterialModule } from './material/material.module';
import { AppRoutingModule } from './app-routing.module';
import { HTTP_INTERCEPTORS, provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { LoginComponent } from './components/login.component';
import { CarparkListComponent } from './components/carpark-list.component';
import { RegisterComponent } from './components/register.component';
import { AuthInterceptorService } from './service/auth-interceptor.service';
import { NavbarComponent } from './components/navbar.component';
import { FavouritesComponent } from './components/favourites.component';
import { CarparkStore } from './store/carpark.store';
import { AuthStore } from './store/auth.store';
import { BackgroundComponent } from './components/background.component';
import { ParkedListComponent } from './components/parked-list.component';
import { ServiceWorkerModule } from '@angular/service-worker';

@NgModule({
  declarations: [
    AppComponent,
    MapComponent,
    LoginComponent,
    CarparkListComponent,
    RegisterComponent,
    NavbarComponent,
    FavouritesComponent,
    BackgroundComponent,
    ParkedListComponent
  ],
  imports: [
    BrowserModule, 
    GoogleMapsModule, 
    ReactiveFormsModule, 
    FormsModule, 
    MaterialModule, 
    AppRoutingModule, ServiceWorkerModule.register('ngsw-worker.js', {
  enabled: !isDevMode(),
  // Register the ServiceWorker as soon as the application is stable
  // or after 30 seconds (whichever comes first).
  registrationStrategy: 'registerWhenStable:30000'
})
  ],
  providers: [
    provideHttpClient(withInterceptorsFromDi()), 
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptorService, multi: true}, 
    CarparkStore, 
    AuthStore
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
