package vttp5a.final_project.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vttp5a.final_project.models.Carpark;
import vttp5a.final_project.models.CarparkDetails;
import vttp5a.final_project.services.CarparkAvailabilityService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping(path = "/api/carparks", produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins="*")
public class CarparkAvailabilityController {
    
    @Autowired
    private CarparkAvailabilityService carparkAvailabilityService;

    @GetMapping("/all")
    @Secured("ROLE_USER")
    public List<Carpark> getAllCarparks(@RequestParam(required = false) String agency) {
        if (agency != null && !agency.isEmpty()) {
            return carparkAvailabilityService.getCarparksByAgency(agency);
        }
        return carparkAvailabilityService.getAllCarparks();
    }

    @GetMapping("/{carparkId}")
    @Secured("ROLE_USER")
    public CarparkDetails getCarparkAvailability(@PathVariable String carparkId) {
        return carparkAvailabilityService.getCarparkAvailabilityById(carparkId);
    }

    @GetMapping("/search")
    @Secured("ROLE_USER")
    public List<Carpark> searchCarparks(@RequestParam(required = false) String searchTerm, @RequestParam(required = false) String agency) {
        if (searchTerm != null && !searchTerm.isEmpty() && agency != null && !agency.isEmpty()) {
            return carparkAvailabilityService.searchCarparks(searchTerm, agency);  // Search with both filters
        } else if (searchTerm != null && !searchTerm.isEmpty()) {
            return carparkAvailabilityService.searchCarparksByTerm(searchTerm);  // Search by term only
        } else if (agency != null && !agency.isEmpty()) {
            return carparkAvailabilityService.getCarparksByAgency(agency);  // Filter by agency only
        } else {
            return carparkAvailabilityService.getAllCarparks();  // Return all carparks if no filters
        }
    }
    
    
}
