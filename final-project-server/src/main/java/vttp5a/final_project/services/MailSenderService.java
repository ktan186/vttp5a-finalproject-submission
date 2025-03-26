package vttp5a.final_project.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mailersend.sdk.MailerSend;
import com.mailersend.sdk.MailerSendResponse;
import com.mailersend.sdk.emails.Email;
import com.mailersend.sdk.exceptions.MailerSendException;

@Service
public class MailSenderService {

    @Value("${ms.api.key}")
    private String msApiKey;
    
    public void sendEmail(String userEmail, String username) {

        System.out.println("MailerSend API Key: " + msApiKey);

        System.out.println("Send to : " + userEmail);


        Email email = new Email();

        email.setFrom("parkingbuddy", "parkingbuddy@trial-p7kx4xw1m08g9yjr.mlsender.net");
        
        email.addRecipient("User", userEmail);

        email.setSubject("Welcome to ParkingBuddy");

        email.setTemplateId("pr9084zoqyelw63d");
        email.addPersonalization("name", username);

        MailerSend ms = new MailerSend();

        ms.setToken(msApiKey);

        try {
        
            MailerSendResponse response = ms.emails().send(email);
            System.out.println(response.messageId);
        } catch (MailerSendException e) {

            e.printStackTrace();
        }
    }
}
