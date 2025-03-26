import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Carpark, CarparkDetails } from '../models';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class CarparkService {
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

  getAllCarparks(agency?: string): Observable<Carpark[]> {
    const headers = this.getAuthHeaders();
    let url =  `${this.apiUrl}/carparks/all`;
    if (agency) {
      url += `?agency=${agency}`;
    }
    return this.httpClient.get<Carpark[]>(url);
  }

  getCarparkById(carparkId: string): Observable<CarparkDetails> {
    const headers = this.getAuthHeaders();
    return this.httpClient.get<CarparkDetails>(`${this.apiUrl}/carparks/${carparkId}`, { headers });
  }

  searchCarparks(searchTerm: string, selectedAgency: string): Observable<Carpark[]> {
    const headers = this.getAuthHeaders();
    let params = new HttpParams();

    if (searchTerm) {
      params = params.set('searchTerm', searchTerm);
    }
    if (selectedAgency) {
      params = params.set('agency', selectedAgency);
    }
    return this.httpClient.get<Carpark[]>(`${this.apiUrl}/carparks/search`, { params });
  }

}
