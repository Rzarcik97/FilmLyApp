package filmly.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import filmly.dto.content.ActorDto;
import filmly.service.ActorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

public class ActorControllerTest extends BaseControllerTest{

    @MockitoBean
    private ActorService actorService;

    @Test
    @DisplayName("""
            GET /actors/popular | Returns list of popular actors
            """)
    void getPopularActors_ShouldReturnActorList() throws Exception {
        // Given
        List<ActorDto> actors = List.of(
                new ActorDto(1L, "Actor One", "/one.jpg"),
                new ActorDto(2L, "Actor Two", "/two.jpg")
        );
        when(actorService.findPopular()).thenReturn(actors);

        // When / Then
        mockMvc.perform(get("/actors/popular"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Actor One"))
                .andExpect(jsonPath("$[0].profile_path").value("/one.jpg"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Actor Two"));
    }

    @Test
    @DisplayName("""
            GET /actors/popular | Returns empty list when no actors available
            """)
    void getPopularActors_EmptyList_ShouldReturnEmptyArray() throws Exception {
        // Given
        when(actorService.findPopular()).thenReturn(List.of());

        // When / Then
        mockMvc.perform(get("/actors/popular"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
}
