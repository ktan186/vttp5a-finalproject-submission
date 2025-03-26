package vttp5a.final_project.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import vttp5a.final_project.models.Carpark;
import vttp5a.final_project.repositories.SqlCarparkRepository;


public class TelegramBotService extends TelegramLongPollingBot {

    @Autowired
    private SqlCarparkRepository sqlCarparkRepo;

    private final Map<Long, Set<String>> userSubscriptions = new HashMap<>();

    public TelegramBotService(@Value("${telegram.bot.token}") String botToken) {
        super(botToken); // Initialize the bot with the token
        System.out.println("Bot initialized with token: " + botToken);
    }
    
    @Override
    public String getBotUsername() {
        return "parkingbuddy_bot";
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println("Received update: " + update);
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            System.out.println("Message: " + message + ", Chat ID: " + chatId);

            if (message.equals("/start")) {
                sendMessage(chatId, "Welcome to ParkingBuddy! Use the following commands:\n" +
                        "/subscribe <carparkId> - Subscribe to a carpark\n" +
                        "/unsubscribe <carparkId> - Unsubscribe from a carpark\n" +
                        "/unsubscribeAll - Unsubscribe from all carparks\n" +
                        "/subscriptions - List your subscribed carparks");
            } else if (message.startsWith("/subscribe ")) {
                String carparkId = message.split(" ")[1];
                try {
                    Carpark c = sqlCarparkRepo.getCarparkById(carparkId);
                    userSubscriptions.computeIfAbsent(chatId, k -> new HashSet<>()).add(carparkId);
                    sendMessage(chatId, "You have subscribed to alerts for: \n Carpark ID: " + carparkId + "\n Carpark Name: " + c.getCarpark_name());
                } catch (Exception e) {
                    sendMessage(chatId, "Invalid Carpark ID! Use:\n" +
                        "/subscribe <carparkId> - Subscribe to a carpark\n" +
                        "/unsubscribe <carparkId> - Unsubscribe from a carpark\n" +
                        "/unsubscribeAll - Unsubscribe from all carparks\n" +
                        "/subscriptions - List your subscribed carparks");
                }
                
            } else if (message.startsWith("/unsubscribe ")) {
                String carparkId = message.split(" ")[1];
                try {
                    Carpark c = sqlCarparkRepo.getCarparkById(carparkId);
                    if (userSubscriptions.containsKey(chatId) && userSubscriptions.get(chatId).remove(carparkId)) {
                        sendMessage(chatId, "You have unsubscribed from: \n Carpark ID: " + carparkId + "\n Carpark Name: " + c.getCarpark_name());
                    } else {
                        sendMessage(chatId, "You were not subscribed to: \n Carpark " + carparkId + "\n Carpark Name: " + c.getCarpark_name());
                    }
                } catch (Exception e) {
                    sendMessage(chatId, "Invalid Carpark ID! Use:\n" +
                        "/subscribe <carparkId> - Subscribe to a carpark\n" +
                        "/unsubscribe <carparkId> - Unsubscribe from a carpark\n" +
                        "/unsubscribeAll - Unsubscribe from all carparks\n" +
                        "/subscriptions - List your subscribed carparks");
                }
                
            }else if (message.equals("/unsubscribeAll")) {
                userSubscriptions.remove(chatId);
                sendMessage(chatId, "You have unsubscribed from all carparks.");
            } else if (message.equals("/subscriptions")) {
                Set<String> subs = userSubscriptions.getOrDefault(chatId, Set.of());
                String msg = "\n";
                for (String s: subs) {
                    Carpark c = sqlCarparkRepo.getCarparkById(s);
                    String indiv = " " + c.getCarpark_name().toString() + " [" + c.getCarpark_id().toString() + "] \n";
                    msg = msg + indiv;
                }
                sendMessage(chatId, "Subscribed carparks: " + msg);
            } else if (update.getMessage().isCommand() && message.startsWith("/start ")) {
                String startParam = message.replace("/start ", "").trim(); // Extract parameter
    
                if (startParam.startsWith("subscribe_")) {
                    String carparkId = startParam.replace("subscribe_", "");
                    userSubscriptions.computeIfAbsent(chatId, k -> new HashSet<>()).add(carparkId);
                    Carpark c = sqlCarparkRepo.getCarparkById(carparkId);
                    sendMessage(chatId, "You have subscribed to alerts for: \n Carpark ID: " + carparkId + "\n Carpark Name: " + c.getCarpark_name());
                } else if (startParam.startsWith("unsubscribe_")) {
                    String carparkId = startParam.replace("unsubscribe_", "");
                    Carpark c = sqlCarparkRepo.getCarparkById(carparkId);
                    if (userSubscriptions.containsKey(chatId) && userSubscriptions.get(chatId).remove(carparkId)) {
                        sendMessage(chatId, "You have unsubscribed from: \n Carpark ID: " + carparkId + "\n Carpark Name: " + c.getCarpark_name());
                    } else {
                        sendMessage(chatId, "You were not subscribed to: \n Carpark " + carparkId + "\n Carpark Name: " + c.getCarpark_name());
                    }
                }
            } else {
                sendMessage(chatId, "Welcome to ParkingBuddy! Use:\n" +
                        "/subscribe <carparkId> - Subscribe to a carpark\n" +
                        "/unsubscribe <carparkId> - Unsubscribe from a carpark\n" +
                        "/unsubscribeAll - Unsubscribe from all carparks\n" +
                        "/subscriptions - List your subscribed carparks");
            }
        }
    }

    public void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void notifySubscribers(String carparkId, String messageText) {
        for (Map.Entry<Long, Set<String>> entry: userSubscriptions.entrySet()) {
            if (entry.getValue().contains(carparkId)) {
                sendMessage(entry.getKey(), messageText);
            }
        }
    }
    
}
