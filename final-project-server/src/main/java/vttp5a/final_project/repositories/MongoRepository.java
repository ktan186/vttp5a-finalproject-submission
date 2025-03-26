package vttp5a.final_project.repositories;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import vttp5a.final_project.models.CarparkAvailability;

@Repository
public class MongoRepository {
    
    @Autowired
    private MongoTemplate mongoTemplate;

    public void dropCarparks() {
        mongoTemplate.dropCollection("carparks");
    }

    public <T> T insertCarpark(T doc) {
        return mongoTemplate.insert(doc, "carparks");
    }

    public List<CarparkAvailability> getAllCarparks() {
        return mongoTemplate.findAll(CarparkAvailability.class, "carparks");
    }

    public List<CarparkAvailability> getCarparkAvailability(String carparkId) {
        Criteria criteria = Criteria.where("carpark_id").is(carparkId);
        Query query = Query.query(criteria);

        List<Document> results = mongoTemplate.find(query, Document.class, "carparks");

        // each carpark id can have multiple entries for different lot types
        List<CarparkAvailability> carparks = new ArrayList<>();
        for (Document d: results) {
            CarparkAvailability c = new CarparkAvailability();
            c.setCarpark_id(d.getString("carpark_id"));
            c.setAvailable_lots(d.getInteger("available_lots"));
            c.setLot_type(d.getString("lot_type"));
            c.setLast_updated(d.getLong("last_updated"));
            carparks.add(c);
        }
        return carparks;
    }

    public List<CarparkAvailability> getLowAvailabilityCarparks(int threshold) {
        Criteria criteria = Criteria.where("available_lots").lte(threshold);
        Query query = Query.query(criteria);

        List<Document> results = mongoTemplate.find(query, Document.class, "carparks");

        List<CarparkAvailability> carparks = new ArrayList<>();
        for (Document d: results) {
            CarparkAvailability c = new CarparkAvailability();
            c.setCarpark_id(d.getString("carpark_id"));
            c.setAvailable_lots(d.getInteger("available_lots"));
            c.setLot_type(d.getString("lot_type"));
            c.setLast_updated(d.getLong("last_updated"));
            carparks.add(c);
        }
        return carparks;
    }
}
