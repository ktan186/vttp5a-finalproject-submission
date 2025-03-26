import { inject, Injectable } from '@angular/core';
import { ComponentStore } from '@ngrx/component-store';
import { tap } from 'rxjs/operators';
import { Carpark } from '../models';
import { CarparkService } from '../service/carpark.service';
import { Observable } from 'rxjs';

export interface CarparkState {
  carparks: Carpark[];
  loading: boolean;
}

@Injectable()
export class CarparkStore extends ComponentStore<CarparkState> {

    private carparkService = inject(CarparkService);

    constructor() {
        super({ carparks: [], loading: false });
    }

    // selectors
    readonly carparks$: Observable<Carpark[]> = this.select(state => state.carparks);

    // updators
    readonly loadCarparks = this.effect(() =>
        this.carparkService.getAllCarparks().pipe(
        tap(carparks => this.patchState({ carparks, loading: false }))
        )
    );

}