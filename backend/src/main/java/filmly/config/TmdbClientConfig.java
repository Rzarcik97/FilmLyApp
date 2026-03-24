package filmly.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class TmdbClientConfig {

    private String token = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI5OGY4NDBiZTViYWRiMWZiNDNmMm"
            + "MxOGZhMGI5OTliYSIsIm5iZiI6MTc3MzkwMDc4OC42MjMwMDAxLCJzdWIiOiI2OWJiOTNmNDdjN"
            + "WVmZmQ3ZjIyM2Q2ZGYiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.qsoztBFU"
            + "_t63zm0ClXviwBp9KBc2JJOvbTrAfA9Nm2M";

    @Bean
    public RestClient tmdbRestClient() {
        String baseUrl = "https://api.themoviedb.org/3";
        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Authorization", "Bearer " + token)
                .defaultHeader("accept", "application/json")
                .build();
    }
}
