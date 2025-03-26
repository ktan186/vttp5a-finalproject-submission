import { ChangeDetectorRef, Component, inject, NgZone, OnInit } from '@angular/core';
import { FavouriteService } from '../service/favourite.service';
import { Carpark, CarparkDetails, FavouriteResponse } from '../models';
import { AuthService } from '../service/auth.service';
import { AuthStore } from '../store/auth.store';
import { CarparkService } from '../service/carpark.service';
import { forkJoin } from 'rxjs';

@Component({
  selector: 'app-favourites',
  standalone: false,
  templateUrl: './favourites.component.html',
  styleUrl: './favourites.component.css'
})
export class FavouritesComponent implements OnInit {

  username!: string;
  carparks: CarparkDetails[] = [];
  
  private favService = inject(FavouriteService);
  private authStore = inject(AuthStore);
  private carparkService = inject(CarparkService);
  private authService = inject(AuthService);

  ngOnInit(): void {
    const token = this.authService.getToken();
    if (token) {
      this.authStore.checkTokenExpiry(token);
    } else {
      this.authService.logout();
    }

    this.authStore.username$.subscribe(username => {
      if (username) {
        this.username = username;
      }
    });
    console.log('username: ' + this.username)
    this.getUserFavourites();
  }

  getUserFavourites(): void {
    // this.favService.getUserFavourites(this.username).subscribe(data => {
    //   this.carparks = data;
    // }, (error) => {
    //   console.error(error);
    // });

    this.favService.getUserFavourites(this.username).subscribe(data => {
      // Retrieve the carpark IDs of the user's favourite carparks
      const carparkIds = data.map(fav => fav.carpark_id);

      // Create an array of observables to fetch carpark details for each carpark_id
      const carparkDetailsObservables = carparkIds.map(carparkId =>
        this.carparkService.getCarparkById(carparkId) // Fetch carpark details
      );

      // Use `forkJoin` to execute all the HTTP requests in parallel and wait for all to complete
      forkJoin(carparkDetailsObservables).subscribe(
        carparkDetailsArray => {
          // Once all details are fetched, store the results in carparks array
          this.carparks = carparkDetailsArray;
          this.carparks.forEach(carpark => {
            carpark.availability.forEach(availability => {
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
          })
        },
        (error) => {
          console.error('Error fetching carpark details:', error);
          alert('Failed to fetch carpark details');
        }
      );
    }, (error) => {
      console.error('Error fetching user favourites:', error);
      // alert('Failed to fetch user favourites');
    });
    
    // this.carparkService.getCarparkById(carparkId).subscribe(
    //   details => {
    //     console.log('Carpark details:', details);
    //     this.carparkDetails = details;
        // this.carparkDetails.availability.forEach(availability => {
          // const singaporeTime = new Date(availability.last_updated);
          // const options: Intl.DateTimeFormatOptions = {
          //   timeZone: 'Asia/Singapore', 
          //   weekday: 'short', 
          //   year: 'numeric', 
          //   month: 'short', 
          //   day: 'numeric', 
          //   hour: 'numeric', 
          //   minute: 'numeric', 
          //   second: 'numeric', 
          //   hour12: true
          // };
          // availability.last_updated = singaporeTime.toLocaleString('en-SG', options);
    //     })
    //     this.cdr.detectChanges();
    //   },
    //   error => {
    //     console.error('Error fetching carpark details:', error);
    //     alert('Failed to fetch carpark details');
    //   }
    // );
  }

  removeFavourite(carparkId: string): void {
    this.favService.removeFavourite(this.username, carparkId).subscribe((response: FavouriteResponse) => {
      console.log(response);
      alert(response.message);
      this.carparks = this.carparks.filter(carpark => carpark.carpark_id !== carparkId);
    }, (error) => {
      console.log(error);
      alert(error.error.message);
    });
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