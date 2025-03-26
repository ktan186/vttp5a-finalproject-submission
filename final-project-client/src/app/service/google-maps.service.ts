import { Injectable } from '@angular/core';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class GoogleMapsService {

  private apiLoaded: boolean = false;

  loadGoogleMapsScript(): Promise<void> {
    if (this.apiLoaded) {
      return Promise.resolve();
    }

    return new Promise((resolve, reject) => {
      const script = document.createElement('script');
      script.src = `https://maps.googleapis.com/maps/api/js?key=${environment.googleMapsApiKey}&libraries=maps,marker,places&v=beta`;
      script.async = true;
      script.defer = true;
      script.onload = () => {
        console.log('Google Maps API loaded');
        this.apiLoaded = true;
        resolve();
      };
      script.onerror = (error: any) => {
        console.error('Error loading Google Maps API', error);
        reject(error);
      };
      document.head.appendChild(script);
    });
  }
}
