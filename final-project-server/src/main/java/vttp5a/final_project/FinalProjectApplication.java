package vttp5a.final_project;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import vttp5a.final_project.services.TelegramBotService;

@SpringBootApplication
@EnableScheduling
public class FinalProjectApplication {

	@Value("${telegram.bot.token}")
    private String botToken;

	public static void main(String[] args) {
		SpringApplication.run(FinalProjectApplication.class, args);
	}

	@Bean
    public TelegramBotService telegramBotService() {
        return new TelegramBotService(botToken);
    }

    @Bean
    public TelegramBotsApi telegramBotsApi(TelegramBotService botService) throws TelegramApiException {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            botsApi.registerBot(botService);
            System.out.println("Bot registered successfully!");
        } catch (TelegramApiException e) {
            System.err.println("Failed to register bot: " + e.getMessage());
            throw e;
        }
        return botsApi;
    }

}
