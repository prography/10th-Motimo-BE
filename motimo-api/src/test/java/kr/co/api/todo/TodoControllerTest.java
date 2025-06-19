package kr.co.api.todo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;
import kr.co.api.config.SecurityConfig;
import kr.co.api.config.WebConfig;
import kr.co.api.security.CustomUserDetailsService;
import kr.co.api.security.jwt.TokenProvider;
import kr.co.api.security.oauth2.CustomOAuth2UserService;
import kr.co.api.security.oauth2.OAuth2AuthenticationFailureHandler;
import kr.co.api.security.oauth2.OAuth2AuthenticationSuccessHandler;
import kr.co.api.security.resolver.AuthTokenArgumentResolver;
import kr.co.api.security.resolver.AuthUserArgumentResolver;
import kr.co.api.todo.rqrs.TodoResultRs;
import kr.co.api.todo.service.TodoCommandService;
import kr.co.api.todo.service.TodoQueryService;
import kr.co.domain.common.exception.AccessDeniedException;
import kr.co.domain.todo.Emotion;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.TodoResult;
import kr.co.domain.todo.exception.TodoErrorCode;
import kr.co.domain.todo.exception.TodoNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TodoController.class)
@Import({SecurityConfig.class, WebConfig.class})
class TodoControllerTest {

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
    private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

    @MockitoBean
    private AuthTokenArgumentResolver authTokenArgumentResolver;

    @Autowired
    private ObjectMapper objectMapper;

    private final UUID userId = UUID.randomUUID();
    private final UUID todoId = UUID.randomUUID();

    @BeforeEach
    void setup() throws Exception {
        reset(authTokenArgumentResolver);
    }

    @Test
    @WithMockUser
    void 투두_결과_제출_성공() throws Exception {
        // given
        String fakeToken = "Bearer test-token";
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.jpg", "image/jpeg", "image".getBytes()
        );

        MockMultipartFile request = new MockMultipartFile(
                "request", "", "application/json", """
                {
                    "emotion": "PROUD",
                    "content": "오늘도 투두 완료!"
                }
                """.trim().getBytes(StandardCharsets.UTF_8)
        );
        TodoResult mockTodoResult = mock(TodoResult.class);

        when(authUserArgumentResolver.supportsParameter(any())).thenReturn(true);
        when(authUserArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(userId);
        when(todoCommandService.submitTodoResult(any(), any(), any(), any(), any())).thenReturn(
                mockTodoResult);

        // when & then
        mockMvc.perform(multipart("/v1/todos/{todoId}/result", todoId)
                        .file(request)
                        .file(file)
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .header("Authorization", fakeToken))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void 투두_결과_제출시_권한이_없는_경우_예외반환() throws Exception {
        // given
        doThrow(new AccessDeniedException(TodoErrorCode.TODO_ACCESS_DENIED))
                .when(todoCommandService)
                .submitTodoResult(eq(userId), eq(todoId), any(), any(), any());

        MockMultipartFile request = new MockMultipartFile(
                "request", "", "application/json", """
                {
                    "emotion": "PROUD",
                    "content": "오늘도 투두 완료!"
                }
                """.trim().getBytes(StandardCharsets.UTF_8)
        );
        when(authUserArgumentResolver.supportsParameter(any())).thenReturn(true);
        when(authUserArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(userId);

        // when & then
        mockMvc.perform(multipart("/v1/todos/{todoId}/result", todoId)
                        .file(request)
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isForbidden());
    }

    @Test
    void 투두_결과_조회_정상() throws Exception {
        // given
        UUID todoResultId = UUID.randomUUID();
        TodoResultRs response = new TodoResultRs(todoResultId, todoId, Emotion.PROUD, "오늘도 투두 완료!",
                "http://file.url");
        when(todoQueryService.getTodoResultByTodoId(todoId)).thenReturn(Optional.of(response));

        // when & then
        mockMvc.perform(get("/v1/todos/{todoId}/result", todoId))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String responseBody = result.getResponse().getContentAsString();
                    TodoResultRs actualResponse = objectMapper.readValue(responseBody,
                            TodoResultRs.class);

                    assertThat(actualResponse.todoResultId()).isEqualTo(response.todoResultId());
                    assertThat(actualResponse.todoId()).isEqualTo(response.todoId());
                    assertThat(actualResponse.content()).isEqualTo(response.content());
                    assertThat(actualResponse.emotion()).isEqualTo(response.emotion());
                    assertThat(actualResponse.fileUrl()).isEqualTo(response.fileUrl());
                });
    }

    @Test
    void 투두_결과가_없는_상태_조회() throws Exception {
        // given
        when(todoQueryService.getTodoResultByTodoId(todoId)).thenReturn(Optional.empty());

        // when & then
        mockMvc.perform(get("/v1/todos/{todoId}/result", todoId))
                .andExpect(status().isNoContent());
    }

    @Test
    void 존재하지않는_투두_아이디로_투두_결과_조회시_예외반환() throws Exception {
        // given
        when(todoQueryService.getTodoResultByTodoId(todoId))
                .thenThrow(new TodoNotFoundException());

        // when & then
        mockMvc.perform(get("/v1/todos/{todoId}/result", todoId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void 투두_완료상태_토글() throws Exception {
        // given
        Todo todo = mock(Todo.class);
        when(authUserArgumentResolver.supportsParameter(any())).thenReturn(true);
        when(authUserArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(userId);
        when(todoCommandService.toggleTodoCompletion(any(), any())).thenReturn(todo);

        // when & then
        mockMvc.perform(patch("/v1/todos/{todoId}/completion", todoId))
                .andExpect(status().isNoContent());

    }

    @Test
    @WithMockUser
    void 투두_완료상태_토글_권한이_없는_경우_예외반환() throws Exception {
        // given
        doThrow(new AccessDeniedException(TodoErrorCode.TODO_ACCESS_DENIED))
                .when(todoCommandService).toggleTodoCompletion(eq(userId), eq(todoId));

        when(authUserArgumentResolver.supportsParameter(any())).thenReturn(true);
        when(authUserArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(userId);

        // when & then
        mockMvc.perform(patch("/v1/todos/{todoId}/completion", todoId))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser
    void 투두_삭제() throws Exception {

        // given
        when(authUserArgumentResolver.supportsParameter(any())).thenReturn(true);
        when(authUserArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(userId);

        // when & then
        mockMvc.perform(delete("/v1/todos/{todoId}", todoId))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void 권한이_없는_유저가_투두_삭제_요청시_예외반환() throws Exception {
        // given
        when(authUserArgumentResolver.supportsParameter(any())).thenReturn(true);
        when(authUserArgumentResolver.resolveArgument(any(), any(), any(), any()))
                .thenReturn(userId);

        doThrow(new AccessDeniedException(TodoErrorCode.TODO_ACCESS_DENIED))
                .when(todoCommandService).deleteById(eq(userId), eq(todoId));

        // when & then
        mockMvc.perform(delete("/v1/todos/{todoId}", todoId))
                .andExpect(status().isForbidden());
    }
}