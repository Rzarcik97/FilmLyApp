package filmly.service.impl;

import filmly.dto.genre.GenreDto;
import filmly.dto.tmdb.TmdbGenreResponse;
import filmly.exception.EntityNotFoundException;
import filmly.mapper.GenreMapper;
import filmly.model.Genre;
import filmly.repository.GenreRepository;
import filmly.service.GenreService;
import java.util.List;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;
    private final RestClient restClient;

    @Override
    public GenreDto getGenreById(Long id) {
        return genreMapper.toDto(genreRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Genre",id)));
    }

    @Override
    public List<GenreDto> getAllGenres() {
        return genreRepository.findAll().stream()
                .map(genreMapper::toDto)
                .toList();
    }

    @Override
    public void syncGenres() {
        List<Genre> movieGenres = fetchGenres("/genre/movie/list");
        List<Genre> tvGenres = fetchGenres("/genre/tv/list");

        List<Genre> all = Stream.concat(movieGenres.stream(), tvGenres.stream())
                .filter(g -> !genreRepository.existsById(g.getId()))
                .distinct()
                .toList();

        genreRepository.saveAll(all);
    }

    private List<Genre> fetchGenres(String uri) {
        TmdbGenreResponse response = restClient.get()
                .uri(uri)
                .retrieve()
                .body(TmdbGenreResponse.class);

        if (response == null || response.genres() == null) {
            return List.of();
        }

        return response.genres().stream()
                .map(genreMapper::toEntity)
                .toList();
    }
}
