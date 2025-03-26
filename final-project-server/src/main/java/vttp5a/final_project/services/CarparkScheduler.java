package vttp5a.final_project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import vttp5a.final_project.models.CarparkAvailability;

@Component
public class CarparkScheduler {
    
    @Autowired
    private CarparkAvailabilityService carparkAvailabilityService;

    @Autowired
    private TelegramBotService telegramBotService;

    @Scheduled(fixedRate = 600000) // update every minute
    public void autoUpdateCarparkAvailability() {
        System.out.println("Fetching latest carpark data from LTA API...");
        carparkAvailabilityService.updateCarparkAvailability();
        System.out.println("Carpark data updated successfully!");
    }

    @Scheduled(fixedRate = 600000) // update every 10 minutes
    public void autoNotifyTelegramSubscribers() {
        List<CarparkAvailability> lowLots = carparkAvailabilityService.getLowAvailabilityCarparks(10);
        for (CarparkAvailability carpark: lowLots) {
            String message = "Alert: Carpark " + carpark.getCarpark_id() + " has low availability (" + carpark.getAvailable_lots() + " lots remaining).";
            telegramBotService.notifySubscribers(carpark.getCarpark_id(), message);
        }
        System.out.println("Alerts sent to telegram!");
    }
}

