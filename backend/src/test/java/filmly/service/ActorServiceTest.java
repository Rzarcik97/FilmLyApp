package filmly.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.util.List;
import filmly.dto.content.ActorDto;
import filmly.dto.tmdb.TmdbPersonResponse;
import filmly.service.impl.ActorServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

@ExtendWith(MockitoExtension.class)
public class ActorServiceTest {

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @InjectMocks
    private ActorServiceImpl actorServiceImpl;

    private void mockRestClientChain() {
        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(any(String.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    // ── findPopular ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("""
            findPopular | Returns list of actors limited to 10
            """)
    void findPopular_ShouldReturnLimitedActorList() {
        // Given
        List<ActorDto> actors = List.of(
                new ActorDto(1L, "Actor One", "/one.jpg"),
                new ActorDto(2L, "Actor Two", "/two.jpg"),
                new ActorDto(3L, "Actor Three", "/three.jpg"),
                new ActorDto(4L, "Actor Four", "/four.jpg"),
                new ActorDto(5L, "Actor Five", "/five.jpg"),
                new ActorDto(6L, "Actor Six", "/six.jpg"),
                new ActorDto(7L, "Actor Seven", "/seven.jpg"),
                new ActorDto(8L, "Actor Eight", "/eight.jpg"),
                new ActorDto(9L, "Actor Nine", "/nine.jpg"),
                new ActorDto(10L, "Actor Ten", "/ten.jpg"),
                new ActorDto(11L, "Actor Eleven", "/eleven.jpg")
        );
        TmdbPersonResponse response = new TmdbPersonResponse(actors);

        mockRestClientChain();
        when(responseSpec.body(TmdbPersonResponse.class)).thenReturn(response);

        // When
        List<ActorDto> result = actorServiceImpl.findPopular();

        // Then
        assertEquals(10, result.size());
        assertEquals("Actor One", result.get(0).name());
    }

    @Test
    @DisplayName("""
            findPopular | Returns empty list when TMDB returns null
            """)
    void findPopular_NullResponse_ShouldReturnEmptyList() {
        // Given
        mockRestClientChain();
        when(responseSpec.body(TmdbPersonResponse.class)).thenReturn(null);

        // When
        List<ActorDto> result = actorServiceImpl.findPopular();

        // Then
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("""
            findPopular | Returns empty list when TMDB returns null results
            """)
    void findPopular_NullResults_ShouldReturnEmptyList() {
        // Given
        TmdbPersonResponse response = new TmdbPersonResponse(null);

        mockRestClientChain();
        when(responseSpec.body(TmdbPersonResponse.class)).thenReturn(response);

        // When
        List<ActorDto> result = actorServiceImpl.findPopular();

        // Then
        assertEquals(0, result.size());
    }

    @Test
    @DisplayName("""
            findPopular | Returns all actors when less than 10
            """)
    void findPopular_LessThanTenActors_ShouldReturnAll() {
        // Given
        List<ActorDto> actors = List.of(
                new ActorDto(1L, "Actor One", "/one.jpg"),
                new ActorDto(2L, "Actor Two", "/two.jpg")
        );
        TmdbPersonResponse response = new TmdbPersonResponse(actors);

        mockRestClientChain();
        when(responseSpec.body(TmdbPersonResponse.class)).thenReturn(response);

        // When
        List<ActorDto> result = actorServiceImpl.findPopular();

        // Then
        assertEquals(2, result.size());
    }
}