
export interface Carpark {
    carpark_id: string;
    carpark_name: string;
    latitude: number;
    longitude: number;
    agency: string;
}

export interface CarparkAvailability {
    carpark_id: string;
    available_lots: number;
    lot_type: string;
    last_updated: string;
}

export interface CarparkDetails {
  carpark_id: string;
  carpark_name: string;
  latitude: number;
  longitude: number;
  agency: string;
  availability: CarparkAvailability[];
}

export interface FavouriteResponse {
  message: string;
}

export interface User {
  username: string;
  email: string;
  password: string;
}

export interface Parked {
  username: string;
  session_id: string;
  carpark_location: string;
  level: number;
  deck: string;
  park_start_time: string;
  notes: string
}
