package filmly.service;

import filmly.model.Content;

public interface ContentService {
    Content getOrCreate(Long externalId, Content.ContentType type);
}
