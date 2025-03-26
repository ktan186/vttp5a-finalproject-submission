import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthStore } from '../store/auth.store';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { User } from '../models';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent implements OnInit {
  // username = '';
  // password = '';
  errorMessage = '';

  private authStore = inject(AuthStore);
  private router = inject(Router);
  private fb = inject(FormBuilder);

  protected form!: FormGroup;

  ngOnInit() {
    // check if user is logged in
    this.authStore.isLoggedIn$.subscribe(isLoggedIn => {
      if (isLoggedIn) {
        this.router.navigate(['/map']);
      }
    });

    this.authStore.error$.subscribe(error => {
      this.errorMessage = error || '';
    });

    this.form = this.createForm();
  }

  private createForm(): FormGroup {
    return this.fb.group({
      username: this.fb.control<string>('', [ Validators.required, Validators.minLength(3) ]), 
      password: this.fb.control<string>('', [ Validators.required, Validators.minLength(3) ])
    })
  }

  protected invalid() {
    return this.form.invalid;
  }
  
  protected hasError(ctrlName: string): boolean {
    const ctrl = this.form.get(ctrlName) as FormControl
    //return !ctrl.pristine && ctrl.invalid
    return ctrl.dirty && ctrl.invalid
  }

  login() {
    const login: User = this.form.value;
    this.errorMessage = '';
    this.authStore.login({ username: login.username, password: login.password });
  }

  goToRegister() {
    this.router.navigate(['/register']);
  }

  hidePassword = true;

  togglePasswordVisibility() {
    this.hidePassword = !this.hidePassword;
    const passwordField = document.querySelector('[formControlName="password"]') as HTMLInputElement;
    if (passwordField) {
      if (this.hidePassword) {
        passwordField.type = 'password';
      } else {
        passwordField.type = 'text';
      }
    }
  }
}
