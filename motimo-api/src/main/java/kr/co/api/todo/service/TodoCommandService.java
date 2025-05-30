package kr.co.api.todo.service;

import jakarta.validation.Valid;
import kr.co.api.todo.rqrs.TodoResultRq;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoCommandService {

    private final TodoRepository todoRepository;

    public void createTodo(UUID userId, UUID subGoalId, String title, LocalDate date) {
        Todo todo = Todo.builder()
                .authorId(userId)
                .subGoalId(subGoalId)
                .title(title)
                .date(date)
                .build();
        todoRepository.save(todo);
    }

    public void submitTodoResult(UUID userId, UUID id, @Valid TodoResultRq request,
            MultipartFile file) {
        // todo: 이미지 업로드 포함해 결과 제출 로직 구현
    }

    public void cancelTodoResult(UUID userId, UUID id) {
        Todo todo = todoRepository.findById(id);
        todo.validateAuthor(userId);
        todo.cancelCompletion();
        todoRepository.save(todo);
    }

    public void deleteById(UUID userId, UUID id) {
        Todo todo = todoRepository.findById(id);
        todo.validateAuthor(userId);
        todoRepository.deleteById(id);
    }
}
