package vttp5a.final_project.models;

public class Parked {
    private String username;
    private String session_id;
    private String carpark_location;
    private Integer level;
    private String deck;
    private Long park_start_time;
    private String notes;

    public Parked() {
    }
    
    public Parked(String username, String session_id, String carpark_location, Integer level, String deck,
            Long park_start_time, String notes) {
        this.username = username;
        this.session_id = session_id;
        this.carpark_location = carpark_location;
        this.level = level;
        this.deck = deck;
        this.park_start_time = park_start_time;
        this.notes = notes;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getSession_id() {
        return session_id;
    }
    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }
    public String getCarpark_location() {
        return carpark_location;
    }
    public void setCarpark_location(String carpark_location) {
        this.carpark_location = carpark_location;
    }
    public Integer getLevel() {
        return level;
    }
    public void setLevel(Integer level) {
        this.level = level;
    }
    public String getDeck() {
        return deck;
    }
    public void setDeck(String deck) {
        this.deck = deck;
    }
    
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getPark_start_time() {
        return park_start_time;
    }

    public void setPark_start_time(Long park_start_time) {
        this.park_start_time = park_start_time;
    }
    
}
