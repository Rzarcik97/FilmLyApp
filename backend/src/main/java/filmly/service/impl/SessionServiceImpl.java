package filmly.service.impl;

import filmly.model.Session;
import filmly.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    // TODO implement logic, change return to DTO, do Exceptions
    @Override
    public Session getSessionById(String id) {
        return null;
    }

    @Override
    public Session createSession(Session session) {
        return null;
    }

    @Override
    public Session updateSession(String id, Session session) {
        return null;
    }

    @Override
    public void deleteSession(String id) {

    }
}
