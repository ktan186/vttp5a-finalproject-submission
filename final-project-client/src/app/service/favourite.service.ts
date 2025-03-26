import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Carpark, FavouriteResponse } from '../models';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class FavouriteService {
  private get apiUrl() {
      return environment.production 
        ? `${environment.apiBaseUrl}/api` 
        : `${environment.apiBaseUrl}/api`;
    }
    
  private httpClient = inject(HttpClient);
  
  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    if (token) {
      return new HttpHeaders().set('Authorization', `Bearer ${token}`);
    } else {
      return new HttpHeaders();
    }
  }

  getUserFavourites(username: string): Observable<Carpark[]> {
    const headers = this.getAuthHeaders();
    return this.httpClient.get<Carpark[]>(`${this.apiUrl}/favourites/${username}`, { headers });
  }

  addFavourite(username: string, carparkId: string): Observable<FavouriteResponse> {
    const headers = this.getAuthHeaders();
    return this.httpClient.post<FavouriteResponse>(`${this.apiUrl}/favourites/${username}?carparkId=${carparkId}`, { headers });
  }

  removeFavourite(username: string, carparkId: string): Observable<FavouriteResponse> {
    const headers = this.getAuthHeaders();
    return this.httpClient.delete<FavouriteResponse>(`${this.apiUrl}/favourites/${username}?carparkId=${carparkId}`, { headers });
  }
}
