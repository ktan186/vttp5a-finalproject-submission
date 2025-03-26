package vttp5a.final_project.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class LtaApi {
    
    @Value("${lta.api.key}")
    private String apiKey;

    private final String API_URL = "https://datamall2.mytransport.sg/ltaodataservice/CarParkAvailabilityv2";
    private final RestTemplate restTemplate = new RestTemplate();

    public String getCarparkData() {
        // System.out.println("API Key: " + apiKey);

        HttpHeaders headers = new HttpHeaders();
        headers.set("AccountKey", apiKey);
        headers.set("Accept", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(API_URL, HttpMethod.GET, entity, String.class);

        String response = responseEntity.getBody();
        return response;
    }
}
