package filmly.service.impl;

import filmly.model.Content;
import filmly.service.ContentService;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {
    // TODO implement logic, change return to DTO, do Exceptions
    @Override
    public Content save(Content movieDto) {
        return null;
    }

    @Override
    public Optional<Content> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Content> findAll() {
        return List.of();
    }

    @Override
    public List<Content> findPopular() {
        return List.of();
    }

    @Override
    public List<Content> findTrending() {
        return List.of();
    }

    @Override
    public List<Content> findRecent() {
        return List.of();
    }

    @Override
    public List<Content> findRecommendations(Long userId) {
        return List.of();
    }

    @Override
    public List<String> findAllGenres() {
        return List.of();
    }

    @Override
    public Content update(Long id, Content movieDto) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
