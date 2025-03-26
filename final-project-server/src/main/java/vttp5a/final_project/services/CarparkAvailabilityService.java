package vttp5a.final_project.services;

import java.io.StringReader;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp5a.final_project.models.Carpark;
import vttp5a.final_project.models.CarparkAvailability;
import vttp5a.final_project.models.CarparkDetails;
import vttp5a.final_project.repositories.MongoRepository;
import vttp5a.final_project.repositories.SqlCarparkRepository;
import vttp5a.final_project.utils.LtaApi;

@Service
public class CarparkAvailabilityService {
    
    @Autowired
    private LtaApi ltaApi;
    
    @Autowired
    private MongoRepository mongoRepository;

    @Autowired
    private SqlCarparkRepository sqlCarparkRepository;

    @Transactional
    public void updateCarparkAvailability() {
        try {
            // Drop MongoDB
            mongoRepository.dropCarparks();

            String jsonResponse = ltaApi.getCarparkData();
            StringReader stringReader = new StringReader(jsonResponse);
            JsonReader jsonReader = Json.createReader(stringReader);
            JsonObject jsonObject = jsonReader.readObject();

            JsonArray jsonValues = jsonObject.getJsonArray("value");
            jsonValues.forEach(d -> {
                JsonObject c = (JsonObject) d;

                // Insert into SQL DB
                Carpark carpark = new Carpark();
                carpark.setCarpark_id(c.getString("CarParkID"));
                carpark.setCarpark_name(c.getString("Development"));
                carpark.setAgency(c.getString("Agency"));

                String location = c.getString("Location");
                String[] coord = location.split(" ");
                // Check if the location contains both latitude and longitude
                if (coord.length != 2) {
                    System.out.println("Skipping document due to invalid coordinates: " + c.toString());
                    return;
                }
                Double latitude = Double.parseDouble(coord[0]);
                Double longitude = Double.parseDouble(coord[1]);
                carpark.setLatitude(latitude);
                carpark.setLongitude(longitude);

                try {
                    sqlCarparkRepository.insertCarpark(carpark);
                } catch (Exception e) {
                    System.out.println("Failed to insert into SQL: " + c.toString());
                    System.err.println(e);
                }
                
                // Insert into MongoDB
                CarparkAvailability carparkAvailability = new CarparkAvailability();
                carparkAvailability.setCarpark_id(c.getString("CarParkID"));
                carparkAvailability.setAvailable_lots(c.getInt("AvailableLots"));
                carparkAvailability.setLot_type(c.getString("LotType"));
                carparkAvailability.setLast_updated(new Date().toInstant().toEpochMilli());

                try {
                    mongoRepository.insertCarpark(carparkAvailability);
                } catch (Exception e) {
                    System.out.println("Failed to insert into MongoDB: " + c.toString());
                    System.err.println(e);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Carpark> getAllCarparks() {
        return sqlCarparkRepository.getAllCarparks();
    }

    public List<Carpark> getCarparksByAgency(String agency) {
        return sqlCarparkRepository.getCarparksByAgency(agency);
    }

    public CarparkDetails getCarparkAvailabilityById(String carparkId) {
        Carpark c = sqlCarparkRepository.getCarparkById(carparkId);
        List<CarparkAvailability> carparkAvailability = mongoRepository.getCarparkAvailability(carparkId);
        
        CarparkDetails cd = new CarparkDetails();
        cd.setCarpark_id(carparkId);
        cd.setCarpark_name(c.getCarpark_name());
        cd.setLatitude(c.getLatitude());
        cd.setLongitude(c.getLongitude());
        cd.setAgency(c.getAgency());
        cd.setAvailability(carparkAvailability);
        return cd;
    }

    public List<CarparkAvailability> getLowAvailabilityCarparks(int threshold) {
        return mongoRepository.getLowAvailabilityCarparks(threshold);
    }

    public List<Carpark> searchCarparks(String searchTerm, String agency) {
        return sqlCarparkRepository.searchCarparks(searchTerm, agency);
    }
    
    public List<Carpark> searchCarparksByTerm(String searchTerm) {
        return sqlCarparkRepository.searchCarparksByTerm(searchTerm);
    }
}
