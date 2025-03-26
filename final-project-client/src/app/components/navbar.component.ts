import { Component, inject, OnInit } from '@angular/core';
import { AuthService } from '../service/auth.service';
import { Router } from '@angular/router';
import { AuthStore } from '../store/auth.store';

@Component({
  selector: 'app-navbar',
  standalone: false,
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent implements OnInit {

  private authStore = inject(AuthStore);
  private authService = inject(AuthService);
  private router = inject(Router);

  isLoggedIn$ = this.authStore.isLoggedIn$;

  ngOnInit(): void {
    const token = this.authService.getToken();
    if (token) {
      this.authStore.checkTokenExpiry(token);
    } else {
      this.authService.logout();
    }
  }

  logout(): void {
    this.authStore.logout();
    this.router.navigate(['/']);
  }

}
