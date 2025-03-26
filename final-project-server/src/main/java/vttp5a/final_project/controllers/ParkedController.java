package vttp5a.final_project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp5a.final_project.models.Parked;
import vttp5a.final_project.services.ParkedService;

@RestController
@RequestMapping(path = "/api/parked", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins="*")
public class ParkedController {
    
    @Autowired
    private ParkedService parkedService;

    @GetMapping("/{username}")
    @Secured("ROLE_USER")
    public ResponseEntity<?> getUserParkingSessions(@PathVariable String username) {
        List<Parked> sessions = parkedService.getUserParkingSessions(username);
        if (sessions.isEmpty()) {
            JsonObject response = Json.createObjectBuilder().add("message", "No parking sessions!").build();
            return ResponseEntity.status(404).body(response.toString());
        }
        sessions.sort((p1, p2) -> p2.getPark_start_time().compareTo(p1.getPark_start_time()));
    
        return ResponseEntity.status(200).body(sessions);
    }

    @PostMapping("/add")
    @Secured("ROLE_USER")
    public ResponseEntity<String> addParkingSession(@RequestBody Parked parked) {
        Boolean isAdded = parkedService.addParkingSession(parked);
        if (isAdded) {
            JsonObject response = Json.createObjectBuilder().add("message", "Parking session added successfully.").build();
            return ResponseEntity.status(201).body(response.toString());
        }
        JsonObject response = Json.createObjectBuilder().add("message", "Failed to add parking session.").build();
        return ResponseEntity.status(400).body(response.toString());
    }

    @DeleteMapping("/delete/{sessionId}")
    @Secured("ROLE_USER")
    public ResponseEntity<String> deleteParkingSession(@PathVariable String sessionId) {
        Boolean isRemoved = parkedService.removeParkingSession(sessionId);
        if (isRemoved) {
            JsonObject response = Json.createObjectBuilder().add("message", "Parking session deleted successfully.").build();
            return ResponseEntity.status(200).body(response.toString());
        }
        JsonObject response = Json.createObjectBuilder().add("message", "Failed to delete parking session.").build();
        return ResponseEntity.status(400).body(response.toString());
    }
}
