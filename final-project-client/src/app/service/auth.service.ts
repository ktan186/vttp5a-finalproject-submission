import { HttpClient, HttpHeaders } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, catchError, Observable, tap } from 'rxjs';
import { environment } from '../environments/environment';

export interface RegisterResponse {
  message: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private get apiUrl() {
    return environment.production 
      ? `${environment.apiBaseUrl}/api` 
      : `${environment.apiBaseUrl}/api`;
  }
  // private apiUrl = environment.apiUrl;
  
  private httpClient = inject(HttpClient);
  private router = inject(Router);

  register(user: { username: string, email: string, password: string }) {
    return this.httpClient.post<RegisterResponse>(
      `${this.apiUrl}/auth/register`, 
      user,
      {
        headers: new HttpHeaders({
          'Content-Type': 'application/json'
        }),
        withCredentials: true
      }
    ).pipe(
      catchError((error) => {
        console.error('Full error:', error);
        // handle already existing user
        if (error.error && error.error.error) {
          throw new Error(error.error.error);
        } else {
          throw new Error('Registration failed. Please try again.');
        }
      })
    );
  }

  login(credentials: any): Observable<{ token: string }> {
    return this.httpClient.post<{ token: string }>(`${this.apiUrl}/auth/login`, credentials,
      {
        headers: new HttpHeaders({
          'Content-Type': 'application/json'
        }),
        withCredentials: true
      }).pipe(
      tap(response => {
        this.setToken(response.token);
      }), catchError((error) => {
          if (error.status === 401 && error.error?.error) {
            throw new Error(error.error.error);
          }
          // Handle other errors
          throw new Error(error.message || 'Login failed');
        })
    )
  }
  
  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    this.router.navigate(['/']);
  }

  setToken(token: string): void {
    localStorage.setItem('token', token);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  setUser(username: string): void {
    localStorage.setItem('user', username);
  }

  getUser(): string | null {
    return localStorage.getItem('user');
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }

  // getCurrentUser(): Observable<any> {
  //   return this.httpClient.get<string>(`/api/auth/me`);
  // }
  

}
