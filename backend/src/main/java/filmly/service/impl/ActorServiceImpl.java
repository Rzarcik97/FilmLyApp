package filmly.service.impl;

import filmly.dto.content.ActorDto;
import filmly.dto.tmdb.TmdbPersonResponse;
import filmly.service.ActorService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class ActorServiceImpl implements ActorService {

    private final RestClient restClient;

    @Override
    public List<ActorDto> findPopular() {
        TmdbPersonResponse response = restClient.get()
                .uri("/person/popular")
                .retrieve()
                .body(TmdbPersonResponse.class);

        if (response == null || response.results() == null) {
            return List.of();
        }

        return response.results().stream()
                .limit(10)
                .toList();
    }
}
