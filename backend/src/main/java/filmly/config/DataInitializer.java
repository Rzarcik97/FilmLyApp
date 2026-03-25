package filmly.config;

import filmly.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.data-initializer.enabled",
        havingValue = "true", matchIfMissing = true)
public class DataInitializer implements ApplicationRunner {

    private final GenreService genreService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        genreService.syncGenres();
    }
}
