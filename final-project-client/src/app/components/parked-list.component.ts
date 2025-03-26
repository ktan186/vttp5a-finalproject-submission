import { Component, inject, OnInit } from '@angular/core';
import { ParkedService } from '../service/parked.service';
import { Parked } from '../models';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../service/auth.service';

@Component({
  selector: 'app-parked-list',
  standalone: false,
  templateUrl: './parked-list.component.html',
  styleUrl: './parked-list.component.css'
})
export class ParkedListComponent implements OnInit {

  username: string = '';
  parkingSessions: Parked[] = [];
  errorMessage: string = '';
  // newSession!: Parked;

  private authService = inject(AuthService);
  private parkedService = inject(ParkedService);
  private fb = inject(FormBuilder);

  protected form!: FormGroup;

  ngOnInit(): void {
    const user = this.authService.getUser();
    if (user) {
      this.username = user;
      this.fetchParkingSessions();
    }
    this.form = this.createForm();
  }

  private createForm(): FormGroup {
    const currentTime = this.getCurrentTimeFormatted();
    return this.fb.group({
      carpark_location: this.fb.control<string>('', [ Validators.required ]), 
      level: this.fb.control<number>(1), 
      deck: this.fb.control<string>(''), 
      parkStartDate: this.fb.control<Date>(new Date(), [ Validators.required ]),
      parkStartTime: this.fb.control<string>(currentTime, [ Validators.required ]),
      notes: this.fb.control<string>('')
    })
  }

  private getCurrentTimeFormatted(): string {
    const now = new Date();
    const hours = now.getHours().toString().padStart(2, '0');
    const minutes = now.getMinutes().toString().padStart(2, '0');
    return `${hours}:${minutes}`;
  }

  protected invalid() {
    return this.form.invalid;
  }
  
  protected hasError(ctrlName: string): boolean {
    const ctrl = this.form.get(ctrlName) as FormControl
    //return !ctrl.pristine && ctrl.invalid
    return ctrl.dirty && ctrl.invalid
  }

  fetchParkingSessions() {
    this.parkedService.getUserParkingSessions(this.username).subscribe(
      (data) => {
        this.parkingSessions = data;
        this.errorMessage = '';
      },
      (error) => {
        if (error.status === 404) {
          this.errorMessage = 'No parking sessions found for this user.';
        } else {
          this.errorMessage = 'An error occurred. Please try again later.';
        }
        this.parkingSessions = [];
      }
    );
  }

  addParkingSession() {
    if (this.form.valid) {
      const formValue = this.form.value;
      
      // Combine date and time
      const combinedDateTime = formValue.parkStartDate instanceof Date 
        ? formValue.parkStartDate 
        : new Date(formValue.parkStartDate);
      
      const [hours, minutes] = formValue.parkStartTime.split(':');
      combinedDateTime.setHours(hours, minutes);
  
      const newSession: Parked = {
        ...this.form.value,
        username: this.username,
        park_start_time: String(combinedDateTime.getTime())
      };
      
      this.parkedService.addParkingSession(newSession).subscribe(
        (response) => {
          console.log('Parking session added:', response);
          this.fetchParkingSessions();
        },
        (error) => {
          console.log('Error adding parking session:', error);
        }
      );
      
      this.form = this.createForm();
    }
  }

  deleteParkingSession(sessionId: string) {
    this.parkedService.deleteParkingSession(sessionId).subscribe(
      (response) => {
        console.log('Parking session deleted:', response);
        this.fetchParkingSessions(); 
      },
      (error) => {
        console.log('Error deleting parking session:', error);
      }
    );
  }
}
