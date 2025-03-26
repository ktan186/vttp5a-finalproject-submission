import { inject, Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FavouriteResponse, Parked } from '../models';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ParkedService {
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

    getUserParkingSessions(username: string): Observable<Parked[]> {
        const headers = this.getAuthHeaders();
        return this.httpClient.get<Parked[]>(`${this.apiUrl}/parked/${username}`, { headers });
    }

    addParkingSession(parked: Parked): Observable<FavouriteResponse> {
        const headers = this.getAuthHeaders();
        return this.httpClient.post<FavouriteResponse>(`${this.apiUrl}/parked/add`, parked, { headers });
    }

    deleteParkingSession(sessionId: string): Observable<FavouriteResponse> {
        const headers = this.getAuthHeaders();
        return this.httpClient.delete<FavouriteResponse>(`${this.apiUrl}/parked/delete/${sessionId}`, { headers});
    }
}
