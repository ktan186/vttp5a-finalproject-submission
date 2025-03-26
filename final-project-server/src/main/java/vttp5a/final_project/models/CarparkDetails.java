package vttp5a.final_project.models;

import java.util.List;

public class CarparkDetails {
    
    private String carpark_id;
    private String carpark_name;
    private double latitude;
    private double longitude;
    private String agency;
    private List<CarparkAvailability> availability;
    
    public CarparkDetails() {
    }

    public CarparkDetails(String carpark_id, String carpark_name, double latitude, double longitude, String agency,
            List<CarparkAvailability> availability) {
        this.carpark_id = carpark_id;
        this.carpark_name = carpark_name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.agency = agency;
        this.availability = availability;
    }

    public String getCarpark_id() {
        return carpark_id;
    }

    public void setCarpark_id(String carpark_id) {
        this.carpark_id = carpark_id;
    }

    public String getCarpark_name() {
        return carpark_name;
    }

    public void setCarpark_name(String carpark_name) {
        this.carpark_name = carpark_name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public List<CarparkAvailability> getAvailability() {
        return availability;
    }

    public void setAvailability(List<CarparkAvailability> availability) {
        this.availability = availability;
    }
    
}
