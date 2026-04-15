package filmly.service;

import filmly.dto.content.ActorDto;
import java.util.List;

public interface ActorService {
    List<ActorDto> findPopular();
}
