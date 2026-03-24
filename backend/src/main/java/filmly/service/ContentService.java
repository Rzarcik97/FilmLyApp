package filmly.service;

import filmly.model.Content;

public interface ContentService {
    Content save(Content movieDto);

    Content update(Long id, Content movieDto);

    void deleteById(Long id);
}
