import { Injectable, inject } from '@angular/core';
import { ComponentStore } from '@ngrx/component-store';
import { AuthService } from '../service/auth.service';
import { switchMap, tap } from 'rxjs/operators';
import { Observable } from 'rxjs';
import { jwtDecode } from "jwt-decode";

export interface AuthState {
  isLoggedIn: boolean;
  token: string | null;
  username: string | null;
  error: string | null;
}

@Injectable()
export class AuthStore extends ComponentStore<AuthState> {
  private authService = inject(AuthService);

  constructor() {
    // initialize with default values
    super({ 
      isLoggedIn: false,
      token: null,
      username: null, 
      error: null
    });

    // update with actual values
    this.patchState({
      isLoggedIn: this.authService.isLoggedIn(),
      token: this.authService.getToken(),
      username: this.authService.getUser()
    });
  }

  // selectors
  readonly isLoggedIn$ = this.select(state => state.isLoggedIn);
  readonly username$ = this.select(state => state.username);
  readonly error$ = this.select(state => state.error);


  // effects
  readonly login = this.effect((credentials$: Observable<{ username: string; password: string }>) =>
    credentials$.pipe(
      tap(() => this.patchState({ 
        isLoggedIn: false, 
        token: null, 
        username: null,
        error: null
      })),
      switchMap(credentials => 
        this.authService.login(credentials).pipe(
          tap({
            next: (response) => {
              console.log('Raw token:', response.token);
              const decoded = jwtDecode(response.token);
              console.log('Decoded token:', decoded);
              
              // fallback to input username if needed
              const username = decoded.sub || credentials.username;
              console.log('Using username:', username);
              
              this.authService.setUser(username);
              this.patchState({ 
                isLoggedIn: true, 
                token: response.token,
                username: decoded.sub,
                error: null
              });
              this.checkTokenExpiry(response.token);
            },
            error: (err) => {
              this.patchState({ 
                error: err.message
              });
            }
          })
        )
      )
    )
  );

  readonly logout = this.effect((trigger$: Observable<void>) =>
    trigger$.pipe(
      tap(() => {
        this.authService.logout();
        this.patchState({ isLoggedIn: false, token: null, username: null });
      })
    ));

  checkTokenExpiry(token: string): void {
    const decoded: any = jwtDecode(token);
    const expiry = decoded.exp * 1000;
    const currentTime = Date.now();
    const timeout = expiry - currentTime;

    if (timeout > 0) {
      setTimeout(() => this.logout(), timeout);
    } else {
      this.logout();
    }
  }
}