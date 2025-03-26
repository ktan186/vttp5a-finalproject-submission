package vttp5a.final_project.configurations;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import vttp5a.final_project.services.TelegramBotService;

@Configuration
public class TelegramBotConfig {

    @Bean
    public TelegramBotsApi telegramBotsApi(TelegramBotService botService) throws TelegramApiException {
        System.out.println("Initializing Telegram Bot API...");
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        try {
            api.registerBot(botService);
            System.out.println("Bot registered successfully with API!");
        } catch (TelegramApiException e) {
            System.err.println("Failed to register bot: " + e.getMessage());
            throw e;
        }
        return api;
    }
}
