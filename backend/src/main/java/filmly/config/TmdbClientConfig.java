package filmly.config;

import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class TmdbClientConfig {

    @Value("${tmdb.read.token}")
    private String token;

    @Bean
    public RestClient tmdbRestClient() {

        HttpClients.custom()
                .disableContentCompression()
                .build();

        String baseUrl = "https://api.themoviedb.org/3";

        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + token)
                .defaultHeader("accept", "application/json")
                .build();
    }
}
