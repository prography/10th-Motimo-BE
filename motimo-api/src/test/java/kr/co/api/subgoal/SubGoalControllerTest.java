package kr.co.api.subgoal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import kr.co.api.config.SecurityConfig;
import kr.co.api.config.WebConfig;
import kr.co.api.security.CustomUserDetailsService;
import kr.co.api.security.jwt.TokenProvider;
import kr.co.api.security.oauth2.CustomOAuth2UserService;
import kr.co.api.security.oauth2.OAuth2AuthenticationSuccessHandler;
import kr.co.api.security.resolver.AuthTokenArgumentResolver;
import kr.co.api.security.resolver.AuthUserArgumentResolver;
import kr.co.api.subgoal.rqrs.TodoCreateRq;
import kr.co.api.todo.service.TodoCommandService;
import kr.co.api.todo.service.TodoQueryService;
import kr.co.domain.common.pagination.CustomSlice;
import kr.co.domain.todo.dto.TodoSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SubGoalController.class)
@Import({SecurityConfig.class, WebConfig.class})
class SubGoalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TodoCommandService todoCommandService;

    @MockitoBean
    private TodoQueryService todoQueryService;

    @MockitoBean
    private TokenProvider tokenProvider;

    @MockitoBean
    private AuthUserArgumentResolver authUserArgumentResolver;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @MockitoBean
    private CustomOAuth2UserService customOAuth2UserService;

    @MockitoBean
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @MockitoBean
    private AuthTokenArgumentResolver authTokenArgumentResolver;

    @Autowired
    private ObjectMapper objectMapper;

    private final UUID userId = UUID.randomUUID();
    private final UUID subGoalId = UUID.randomUUID();

    @BeforeEach
    void setup() throws Exception {
        reset(authTokenArgumentResolver);
    }

    @Test
    @WithMockUser
    void 투두_생성_성공() throws Exception {
        // given
        TodoCreateRq request = new TodoCreateRq("투두", LocalDate.now());
        String requestJson = objectMapper.writeValueAsString(request);
        when(authUserArgumentResolver.supportsParameter(any())).thenReturn(true);
        when(authUserArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(userId);

        // when & then
        mockMvc.perform(post("/v1/sub-goals/{subGoalId}/todo", subGoalId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .with(csrf()))
                .andExpect(status().isCreated());

        verify(todoCommandService).createTodo(userId, subGoalId, request.title(), request.date());
    }

    @Test
    @WithMockUser
    void 세부목표_todo_목록_조회_성공() throws Exception {
        // given
        int page = 0;
        int size = 10;
        UUID todoId1 = UUID.randomUUID();
        UUID todoId2 = UUID.randomUUID();
        List<TodoSummary> todoSummaries = Arrays.asList(
                createMockTodoSummary(todoId1, "투두1"),
                createMockTodoSummary(todoId2, "투두2")
        );

        CustomSlice<TodoSummary> expectedSlice = new CustomSlice<>(todoSummaries, false);
        when(authUserArgumentResolver.supportsParameter(any())).thenReturn(true);
        when(authUserArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(userId);
        when(todoQueryService.getTodosBySubGoal(subGoalId, page, size))
                .thenReturn(expectedSlice);

        // when & then
        mockMvc.perform(get("/v1/sub-goals/{subGoalId}/todos", subGoalId)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    TypeReference<CustomSlice<Map<String, Object>>> typeRef = new TypeReference<CustomSlice<Map<String, Object>>>() {
                    };
                    CustomSlice<Map<String, Object>> actualResponse =
                            objectMapper.readValue(responseBody, typeRef);

                    // 응답 검증
                    assertThat(actualResponse.content().size()).isEqualTo(todoSummaries.size());
                    assertThat(actualResponse.hasNext()).isFalse();
                })
                .andExpect(jsonPath("$.content[0].id").value(todoId1.toString()))
                .andExpect(jsonPath("$.content[0].title").value("투두1"))
                .andExpect(jsonPath("$.content[1].id").value(todoId2.toString()))
                .andExpect(jsonPath("$.content[1].title").value("투두2"));

        verify(todoQueryService).getTodosBySubGoal(subGoalId, page, size);
    }

    private TodoSummary createMockTodoSummary(UUID id, String title) {
        return new TodoSummary(id, title, LocalDate.now(), false, LocalDateTime.now(), false);
    }
}