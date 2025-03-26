import { Component, inject, OnInit } from '@angular/core';
import { CarparkService } from '../service/carpark.service';
import { Carpark, FavouriteResponse } from '../models';
import { AuthService } from '../service/auth.service';
import { FavouriteService } from '../service/favourite.service';
import { AuthStore } from '../store/auth.store';

@Component({
  selector: 'app-carpark-list',
  standalone: false,
  templateUrl: './carpark-list.component.html',
  styleUrl: './carpark-list.component.css'
})
export class CarparkListComponent implements OnInit {
  carparks: Carpark[] = [];
  selectedAgency!: '';
  agencies = ['LTA', 'HDB', 'URA'];
  searchTerm!: '';

  private carparkService = inject(CarparkService);
  private authService = inject(AuthService);
  private favService = inject(FavouriteService);
  private authStore = inject(AuthStore);

  ngOnInit(): void {
    const token = this.authService.getToken();
    if (token) {
      this.authStore.checkTokenExpiry(token);
    } else {
      this.authService.logout();
    }
    this.loadCarparks();
  }

  addFavourite(carparkId: string): void {
    const username = this.authService.getUser();
    console.log('username: ' + username);
    if (username) {
      this.favService.addFavourite(username, carparkId).subscribe((response: FavouriteResponse) => {
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
  
  filterCarparks() {
    this.loadCarparks();
  }

  searchCarparks() {
    this.loadCarparks();
  }

  private loadCarparks() {
    this.carparkService.searchCarparks(this.searchTerm, this.selectedAgency).subscribe(carparks => this.carparks = carparks);
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
