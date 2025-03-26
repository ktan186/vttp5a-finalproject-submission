import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../service/auth.service';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { User } from '../models';

@Component({
  selector: 'app-register',
  standalone: false,
  templateUrl: './register.component.html',
  styleUrl: './register.component.css'
})
export class RegisterComponent implements OnInit {

  errorMessage = '';
  successMessage = '';
  hidePassword = true;

  private authService = inject(AuthService);
  private router = inject(Router);
  private fb = inject(FormBuilder);

  protected form!: FormGroup;

  ngOnInit(): void {
    this.form = this.createForm();
  }

  private createForm(): FormGroup {
      return this.fb.group({
        username: this.fb.control<string>('', [ Validators.required, Validators.minLength(3) ]), 
        email: this.fb.control<string>('', [ Validators.required, Validators.email ]), 
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

  register() {
    const register: User = this.form.value;

    this.errorMessage = '';
    this.successMessage = '';

    this.authService.register({ username: register.username, email: register.email, password: register.password })
      .subscribe({
        next: (response) => {
          this.successMessage = response.message;
          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 2000);
        }, 
        error: (err) => {
          this.errorMessage = err.message;
          console.error('Registration failed', err);
        }
      });
  }

  goToLogin() {
    this.router.navigate(['/']);
  }
}
