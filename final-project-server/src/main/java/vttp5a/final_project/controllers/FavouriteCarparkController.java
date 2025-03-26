package vttp5a.final_project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import vttp5a.final_project.models.Carpark;
import vttp5a.final_project.services.FavouriteCarparkService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping(path = "/api/favourites", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins="*")
public class FavouriteCarparkController {
    
    @Autowired
    private FavouriteCarparkService favouriteCarparkService;

    @GetMapping("/{username}")
    @Secured("ROLE_USER")
    public ResponseEntity<?> getUserFavourites(@PathVariable String username) {
        List<Carpark> favourites = favouriteCarparkService.getUserFavourites(username);
        if (favourites.isEmpty()) {
            JsonObject response = Json.createObjectBuilder().add("message", "No favourited carparks!").build();
            return ResponseEntity.status(404).body(response.toString());
        }
        return ResponseEntity.status(200).body(favourites);
    }

    @PostMapping("/{username}")
    @Secured("ROLE_USER")
    public ResponseEntity<String> addFavourite(@PathVariable String username, @RequestParam String carparkId) {
        try {
            Boolean added = favouriteCarparkService.addFavourite(username, carparkId);
            if (added) {
                JsonObject response = Json.createObjectBuilder().add("message", "Successfully favourited carpark!").build();
                return ResponseEntity.status(201).body(response.toString());
            } else {
                JsonObject response = Json.createObjectBuilder().add("message", "Error adding carpark to favourites.").build();
                return ResponseEntity.status(500).body(response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject response = Json.createObjectBuilder().add("message", "Carpark already in your favourites.").build();
            return ResponseEntity.status(400).body(response.toString());
        }
    }
    
    @DeleteMapping("/{username}")
    @Secured("ROLE_USER")
    public ResponseEntity<String> removeFavourite(@PathVariable String username, @RequestParam String carparkId) {
        try {
            Boolean deleted = favouriteCarparkService.removeFavourite(username, carparkId);
            if (deleted) {
                JsonObject response = Json.createObjectBuilder().add("message", "Favourite removed!").build();
                return ResponseEntity.status(200).body(response.toString());
            } else {
                JsonObject response = Json.createObjectBuilder().add("message", "Carpark not found in your favourites.").build();
                return ResponseEntity.status(404).body(response.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            JsonObject response = Json.createObjectBuilder().add("message", "Error removing favourite carpark: " + e.getMessage()).build();
            return ResponseEntity.status(500).body(response.toString());
        }
    }

}
