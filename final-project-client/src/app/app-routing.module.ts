import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Routes } from '@angular/router';
import { MapComponent } from './components/map.component';
import { LoginComponent } from './components/login.component';
import { AuthGuard } from './guards/auth.guard';
import { CarparkListComponent } from './components/carpark-list.component';
import { RegisterComponent } from './components/register.component';
import { FavouritesComponent } from './components/favourites.component';
import { ParkedListComponent } from './components/parked-list.component';

const routes: Routes = [
  {path: "", component: LoginComponent}, 
  {path: "register", component: RegisterComponent}, 
  {path: "map", component: MapComponent, canActivate: [AuthGuard]}, 
  {path: "list", component: CarparkListComponent, canActivate: [AuthGuard]},
  {path: "favourites", component: FavouritesComponent, canActivate: [AuthGuard]}, 
  {path: "parked", component: ParkedListComponent, canActivate: [AuthGuard]}, 
  {path: "**", redirectTo:"/", pathMatch: "full"}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)], 
  exports: [RouterModule]
})

export class AppRoutingModule { }
