package kr.co.api.todo.service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;
import kr.co.domain.common.event.Events;
import kr.co.domain.common.event.FileDeletedEvent;
import kr.co.domain.common.event.FileRollbackEvent;
import kr.co.domain.todo.Emotion;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.TodoResult;
import kr.co.domain.todo.repository.TodoRepository;
import kr.co.domain.todo.repository.TodoResultRepository;
import kr.co.infra.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class TodoCommandService {

    private final TodoRepository todoRepository;
    private final TodoResultRepository todoResultRepository;
    private final StorageService storageService;

    public UUID createTodo(UUID userId, UUID subGoalId, String title, LocalDate date) {
        Todo todo = Todo.createTodo()
                .userId(userId)
                .subGoalId(subGoalId)
                .title(title)
                .date(date)
                .build();
        return todoRepository.create(todo).getId();
    }

    public UUID submitTodoResult(UUID userId, UUID todoId, Emotion emotion, String content,
            MultipartFile file) {
        Todo todo = todoRepository.findById(todoId);
        todo.validateOwner(userId);
        Optional<TodoResult> todoResult = todoResultRepository.findByTodoId(todoId);

        String filePath = "";
        if (file != null && !file.isEmpty()) {
            filePath = String.format("todo/%s/%s", todoId, UUID.randomUUID());
            storageService.store(file, filePath);
            // 이미지 삭제 이벤트 발행 (트랜잭션 롤백 시 동작)
            Events.publishEvent(new FileRollbackEvent(filePath));
        }

        TodoResult result = TodoResult.createTodoResult()
                .todoId(todoId)
                .userId(userId)
                .emotion(emotion)
                .content(content)
                .filePath(filePath)
                .build();

        return todoResultRepository.create(result).getId();
    }

    public UUID toggleTodoCompletion(UUID userId, UUID todoId) {
        Todo todo = todoRepository.findById(todoId);
        todo.validateOwner(userId);
        todo.toggleCompletion();
        return todoRepository.update(todo).getId();
    }

    public UUID updateTodo(UUID userId, UUID todoId, String title, LocalDate date) {
        Todo todo = todoRepository.findById(todoId);
        todo.validateOwner(userId);
        todo.update(title, date);
        return todoRepository.update(todo).getId();
    }

    public TodoResult updateTodoResult(UUID userId, UUID todoResultId, Emotion emotion,
            String content, MultipartFile file) {
        TodoResult todoResult = todoResultRepository.findById(todoResultId);
        todoResult.validateOwner(userId);

        String filePath = "";
        if (file != null && !file.isEmpty()) {
            String originalFilePath = todoResult.getFilePath();

            filePath = String.format("todo/%s/%s", todoResult.getTodoId(),
                    UUID.randomUUID());
            storageService.store(file, filePath);

            if (originalFilePath != null && !originalFilePath.isBlank()) {
                Events.publishEvent(new FileDeletedEvent(originalFilePath));
            }
        }

        todoResult.update(emotion, content, filePath);
        return todoResultRepository.save(todoResult);
    }

    public void deleteById(UUID userId, UUID todoId) {
        Todo todo = todoRepository.findById(todoId);
        todo.validateOwner(userId);
        todoResultRepository.findByTodoId(todoId)
                .ifPresent(todoResult -> {
                    todoResult.validateOwner(userId);
                    deleteTodoResult(todoResult);
                });
        todoRepository.deleteById(todoId);
    }

    public void deleteTodoResultById(UUID userId, UUID todoResultId) {
        TodoResult todoResult = todoResultRepository.findById(todoResultId);
        todoResult.validateOwner(userId);
        deleteTodoResult(todoResult);
    }

    private void deleteTodoResult(TodoResult todoResult) {
        if (todoResult.getFilePath() != null && !todoResult.getFilePath().isBlank()) {
            Events.publishEvent(new FileDeletedEvent(todoResult.getFilePath()));
        }
        todoResultRepository.deleteById(todoResult.getId());
    }
}
