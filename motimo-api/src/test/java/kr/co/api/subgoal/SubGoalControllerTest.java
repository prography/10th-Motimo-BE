package kr.co.api.subgoal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import kr.co.api.config.SecurityConfig;
import kr.co.api.config.WebConfig;
import kr.co.api.security.CustomUserDetailsService;
import kr.co.api.security.jwt.TokenProvider;
import kr.co.api.security.oauth2.CustomOAuth2AuthorizationRequestRepository;
import kr.co.api.security.oauth2.CustomOAuth2UserService;
import kr.co.api.security.oauth2.OAuth2AuthenticationFailureHandler;
import kr.co.api.security.oauth2.OAuth2AuthenticationSuccessHandler;
import kr.co.api.security.resolver.AuthTokenArgumentResolver;
import kr.co.api.security.resolver.AuthUserArgumentResolver;
import kr.co.api.subgoal.rqrs.TodoCreateRq;
import kr.co.api.subgoal.service.SubGoalCommandService;
import kr.co.api.todo.service.TodoCommandService;
import kr.co.api.todo.service.TodoQueryService;
import kr.co.domain.common.pagination.CustomSlice;
import kr.co.domain.todo.Emotion;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.TodoStatus;
import kr.co.domain.todo.dto.TodoItemDto;
import kr.co.domain.todo.dto.TodoResultItemDto;
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
    private SubGoalCommandService subGoalCommandService;

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
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @MockitoBean
    private AuthTokenArgumentResolver authTokenArgumentResolver;

    @MockitoBean
    private CustomOAuth2AuthorizationRequestRepository authorizationRequestRepository;

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
        UUID todoId = UUID.randomUUID();
        Todo todo = mock(Todo.class);
        String requestJson = objectMapper.writeValueAsString(request);
        when(authUserArgumentResolver.supportsParameter(any())).thenReturn(true);
        when(authUserArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(userId);
        when(todoCommandService.createTodo(any(), any(), any(), any())).thenReturn(todoId);

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
    void 세부목표_완료되지_않은_상태거나_오늘의_todo_목록_조회_성공() throws Exception {
        // given
        UUID todoId1 = UUID.randomUUID();
        UUID todoId2 = UUID.randomUUID();
        int offset = 0;
        int size = 10;

        List<TodoItemDto> todoItemDtos = Arrays.asList(
                createMockTodoItem(todoId1, "투두1"),
                createMockTodoItem(todoId2, "투두2")
        );

        CustomSlice<TodoItemDto> mockSlice = new CustomSlice<>(todoItemDtos, true, offset, size);

        when(todoQueryService.getIncompleteOrTodayTodosBySubGoalIdWithSlice(subGoalId, offset,
                size))
                .thenReturn(mockSlice);

        mockMvc.perform(get("/v1/sub-goals/{subGoalId}/todos/incomplete-or-date", subGoalId)
                        .param("offset", String.valueOf(offset))
                        .param("size", String.valueOf(size))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].id").value(todoId1.toString()))
                .andExpect(jsonPath("$.content[0].title").value("투두1"))
                .andExpect(jsonPath("$.content[1].id").value(todoId2.toString()))
                .andExpect(jsonPath("$.content[1].title").value("투두2"))
                .andExpect(jsonPath("$.hasNext").value(true))
                .andExpect(jsonPath("$.offset").value(offset))
                .andExpect(jsonPath("$.size").value(size));
        verify(todoQueryService)
                .getIncompleteOrTodayTodosBySubGoalIdWithSlice(subGoalId, offset, size);
    }

    private TodoItemDto createMockTodoItem(UUID id, String title) {
        return new TodoItemDto(id, title, LocalDate.now(), TodoStatus.INCOMPLETE,
                LocalDateTime.now(),
                new TodoResultItemDto(
                        UUID.randomUUID(),
                        Emotion.PROUD,
                        "todo result",
                        "", "", ""
                ));
    }
}