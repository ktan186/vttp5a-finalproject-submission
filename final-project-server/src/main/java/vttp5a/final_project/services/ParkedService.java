package vttp5a.final_project.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vttp5a.final_project.models.Parked;
import vttp5a.final_project.repositories.SqlParkedRepository;

@Service
public class ParkedService {
    
    @Autowired
    private SqlParkedRepository sqlParkedRepo;

    public List<Parked> getUserParkingSessions(String username) {
        return sqlParkedRepo.getParkingDetailsByUser(username);
    }

    @Transactional
    public Boolean addParkingSession(Parked parked) {
        String sessionId = UUID.randomUUID().toString().substring(0, 7);
        parked.setSession_id(sessionId);
        Boolean addedSession = sqlParkedRepo.addParkingSession(parked);
        Boolean addedDetails = sqlParkedRepo.addParkingDetails(parked);
        return addedSession && addedDetails;
    }

    @Transactional
    public Boolean removeParkingSession(String sessionId) {
        Boolean removedDetails = sqlParkedRepo.removeParkingDetails(sessionId);
        Boolean removedSession = sqlParkedRepo.removeParkingSession(sessionId);
        return removedDetails && removedSession;
    }
}
