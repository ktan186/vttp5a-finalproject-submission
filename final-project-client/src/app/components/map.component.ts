import { ChangeDetectorRef, Component, inject, OnInit } from '@angular/core';
import { GoogleMapsService } from '../service/google-maps.service';
import { CarparkService } from '../service/carpark.service';
import { Router } from '@angular/router';
import { Carpark, CarparkDetails, FavouriteResponse } from '../models';
import { FavouriteService } from '../service/favourite.service';
import { AuthService } from '../service/auth.service';
import { CarparkStore } from '../store/carpark.store';
import { Observable } from 'rxjs';
import { AuthStore } from '../store/auth.store';

@Component({
  selector: 'app-map',
  standalone: false,
  templateUrl: './map.component.html',
  styleUrl: './map.component.css'
})
export class MapComponent implements OnInit {
  map!: google.maps.Map;
  center: google.maps.LatLngLiteral = { lat: 1.3521, lng: 103.8198 }; // Singapore center
  zoom = 12;
  markers: { position: google.maps.LatLngLiteral, title: string, carparkId: string, icon: any  }[] = [];
  apiLoaded = false;
  carparkDetails!: CarparkDetails;
  destination = '';

  private googleMapsService = inject(GoogleMapsService);
  private carparkService = inject(CarparkService);
  private router = inject(Router);
  private cdr = inject(ChangeDetectorRef);
  private favService = inject(FavouriteService);
  private authService = inject(AuthService);
  private carparkStore = inject(CarparkStore);
  private authStore = inject(AuthStore);

  carparks$: Observable<Carpark[]> = this.carparkStore.carparks$;

  ngOnInit(): void {
    const token = this.authService.getToken();
    if (token) {
      this.authStore.checkTokenExpiry(token);
    } else {
      this.authService.logout();
    }

    this.googleMapsService.loadGoogleMapsScript().then(() => {
      this.apiLoaded = true; 
      console.log('Google Maps API loaded');
      this.initializeAutocomplete();
    }).catch(error => {
      console.error('Error loading Google Maps API', error);
    });

    this.carparkStore.loadCarparks();
    console.log('Store loaded');

    this.carparks$.subscribe(carparks => {
      this.loadCarpark(carparks);
    });
  }

  getMarkerColor(agency: string): string {
    switch (agency) {
      case 'URA':
        return '#ffa333'; // orange
      case 'HDB':
        return '#FF5733'; // red
      case 'LTA':
        return '#4154d1'; // blue
      default:
        return '#808080'; // gray
    }
  }

  loadCarpark(carparks: Carpark[]): void {
    this.markers = carparks.map(carpark => {
      let markerColor = this.getMarkerColor(carpark.agency);

      return {
        position: { lat: carpark.latitude, lng: carpark.longitude },
        title: carpark.carpark_name,
        carparkId: carpark.carpark_id,
        icon: {
          path: google.maps.SymbolPath.BACKWARD_CLOSED_ARROW,
          fillColor: markerColor,
          fillOpacity: 0.9,
          strokeColor: markerColor,
          strokeWeight: 1,
          scale: 5
        }
      };
    });
    this.cdr.detectChanges();
  }
  

  initializeAutocomplete(): void {
    const input = document.getElementById('destination-input') as HTMLInputElement;
    const autocomplete = new google.maps.places.Autocomplete(input, {
      componentRestrictions: { country: 'SG' },
      types: ['geocode']
    });

    autocomplete.addListener('place_changed', () => {
      const place = autocomplete.getPlace();
      if (place.geometry && place.geometry.location) {
        this.center = {
          lat: place.geometry.location.lat(),
          lng: place.geometry.location.lng()
        };
        this.zoom = 17;
      }
    });
  }

  search() {
    if (this.destination) {
      const geocoder = new google.maps.Geocoder();
      geocoder.geocode({ address: this.destination }, (results, status) => {
        if (status === 'OK' && results && results.length > 0 && results[0].geometry) {
          this.center = {
            lat: results[0].geometry.location.lat(), 
            lng: results[0].geometry.location.lng()
          };
          this.zoom = 17;
          this.cdr.detectChanges();
        } else {
          alert('Location not found');
        }
      });
    }
  }

  onMarkerClick(carparkId: string): void {
    console.log('Marker clicked: ', carparkId);
    this.carparkService.getCarparkById(carparkId).subscribe(
      details => {
        console.log('Carpark details:', details);
        this.carparkDetails = details;
        this.carparkDetails.availability.forEach(availability => {
          const singaporeTime = new Date(availability.last_updated);
          const options: Intl.DateTimeFormatOptions = {
            timeZone: 'Asia/Singapore', 
            weekday: 'short', 
            year: 'numeric', 
            month: 'short', 
            day: 'numeric', 
            hour: 'numeric', 
            minute: 'numeric', 
            second: 'numeric', 
            hour12: true
          };
          availability.last_updated = singaporeTime.toLocaleString('en-SG', options);
        })
        this.cdr.detectChanges();
      },
      error => {
        console.error('Error fetching carpark details:', error);
        alert('Failed to fetch carpark details');
      }
    );
  }

  addFavourite(): void {
    const username = this.authService.getUser();
    console.log('username: ' + username);
    if (username) {
      this.favService.addFavourite(username, this.carparkDetails.carpark_id).subscribe((response: FavouriteResponse) => {
        console.log(response);
        alert(response.message);
      }, (error) => {
        console.log(error);
        alert(error.error.message);
      });
    } else {
      console.log('no username');
    }
  }

  subscribeToTelegram(carparkId: string): void {
    const encodedMessage = encodeURIComponent(`subscribe_${carparkId}`);
    const telegramUrl = `https://t.me/parkingbuddy_bot?start=${encodedMessage}`;
    window.open(telegramUrl, '_blank');
  }
  
  unsubscribeFromTelegram(carparkId: string): void {
    const encodedMessage = encodeURIComponent(`unsubscribe_${carparkId}`);
    const telegramUrl = `https://t.me/parkingbuddy_bot?start=${encodedMessage}`;
    window.open(telegramUrl, '_blank');
  }

  navigateTo(carpark: any) {
    window.open(`https://www.google.com/maps/dir/?api=1&destination=${carpark.latitude},${carpark.longitude}`);
  }
  
}

