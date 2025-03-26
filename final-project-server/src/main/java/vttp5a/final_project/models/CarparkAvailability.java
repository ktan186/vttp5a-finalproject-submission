package vttp5a.final_project.models;

public class CarparkAvailability {
    
    private String carpark_id;
    private int available_lots;
    private String lot_type;
    private Long last_updated;

    public CarparkAvailability() {
    }

    public CarparkAvailability(String carpark_id, int available_lots, String lot_type, Long last_updated) {
        this.carpark_id = carpark_id;
        this.available_lots = available_lots;
        this.lot_type = lot_type;
        this.last_updated = last_updated;
    }

    public String getCarpark_id() {
        return carpark_id;
    }

    public void setCarpark_id(String carpark_id) {
        this.carpark_id = carpark_id;
    }

    public int getAvailable_lots() {
        return available_lots;
    }

    public void setAvailable_lots(int available_lots) {
        this.available_lots = available_lots;
    }

    public String getLot_type() {
        return lot_type;
    }

    public void setLot_type(String lot_type) {
        this.lot_type = lot_type;
    }

    public Long getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(Long last_updated) {
        this.last_updated = last_updated;
    }

    
}
