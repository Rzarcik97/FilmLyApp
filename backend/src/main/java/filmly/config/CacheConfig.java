package filmly.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager();
        manager.setCacheNames(List.of(
                "genres", "genreById",
                "popularMovies", "trendingMovies", "recentMovies", "upcomingMovies",
                "popularSeries", "trendingSeries", "recentSeries"
        ));
        manager.setCaffeine(Caffeine.newBuilder()
                .expireAfterWrite(20, TimeUnit.MINUTES)
                .maximumSize(500));
        return manager;
    }
}
