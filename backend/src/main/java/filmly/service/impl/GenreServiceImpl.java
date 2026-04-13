package filmly.service.impl;

import filmly.dto.genre.GenreDto;
import filmly.dto.tmdb.TmdbGenreResponse;
import filmly.enums.GenreType;
import filmly.exception.EntityNotFoundException;
import filmly.mapper.GenreMapper;
import filmly.model.Genre;
import filmly.repository.GenreRepository;
import filmly.service.GenreService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
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
        if (genreRepository.count() > 0) {
            return;
        }
        List<Genre> movieGenres = fetchGenres("/genre/movie/list", GenreType.MOVIE);
        List<Genre> tvGenres = fetchGenres("/genre/tv/list", GenreType.TV);

        genreRepository.saveAll(movieGenres);

        Map<Long, Genre> tvGenreMap = tvGenres.stream()
                .collect(Collectors.toMap(Genre::getId, g -> g));

        Map<Long, Genre> movieGenreMap = movieGenres.stream()
                .collect(Collectors.toMap(Genre::getId, g -> g));

        movieGenreMap.forEach((id, genre) -> {
            if (tvGenreMap.containsKey(id)) {
                genre.setType(GenreType.BOTH);
            }
        });

        Map<Long, Genre> allGenres = new HashMap<>(movieGenreMap);
        tvGenreMap.forEach(allGenres::putIfAbsent);

        genreRepository.saveAll(allGenres.values());
    }

    private List<Genre> fetchGenres(String uri, GenreType type) {
        TmdbGenreResponse response = restClient.get()
                .uri(uri)
                .retrieve()
                .body(TmdbGenreResponse.class);

        if (response == null || response.genres() == null) {
            return List.of();
        }

        return response.genres().stream()
                .map(g -> {
                    Genre genre = genreMapper.toEntity(g);
                    genre.setType(type);
                    return genre;
                })
                .toList();
    }
}
