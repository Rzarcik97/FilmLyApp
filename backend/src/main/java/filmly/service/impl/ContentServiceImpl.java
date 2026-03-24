package filmly.service.impl;

import filmly.model.Content;
import filmly.service.ContentService;
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
    public Content update(Long id, Content movieDto) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }
}
