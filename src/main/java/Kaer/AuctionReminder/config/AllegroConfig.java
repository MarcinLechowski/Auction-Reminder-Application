package Kaer.AuctionReminder.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AllegroConfig {
    @Value("${allegro.api-url}")
    private String apiUrl;

    @Value("${allegro.access-token}")
    private String accessToken;

    public String getApiUrl() {
        return apiUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }
}