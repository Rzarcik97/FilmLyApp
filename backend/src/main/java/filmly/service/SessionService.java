package filmly.service;

import filmly.model.Session;

public interface SessionService {

    Session getSessionById(String id);

    Session createSession(Session session);

    Session updateSession(String id, Session session);

    void deleteSession(String id);
}
