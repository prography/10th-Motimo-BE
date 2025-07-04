package kr.co.api.todo.service;

import java.time.LocalDate;
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

    public UUID upsertTodoResult(UUID userId, UUID todoId, Emotion emotion, String content,
            MultipartFile file) {
        Todo todo = todoRepository.findById(todoId);
        todo.validateOwner(userId);

        return todoResultRepository.findByTodoId(todoId)
                .map(todoResult -> updateTodoResult(todoResult, userId, emotion, content, file))
                .orElseGet(() -> createTodoResult(userId, todoId, emotion, content, file));
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

    private UUID createTodoResult(UUID userId, UUID todoId, Emotion emotion, String content,
            MultipartFile file) {
        String filePath = "";
        if (file != null && !file.isEmpty()) {
            filePath = String.format("todo/%s/%s", todoId, UUID.randomUUID());
            storageService.store(file, filePath);
            Events.publishEvent(new FileRollbackEvent(filePath));
        }

        TodoResult result = TodoResult.createTodoResult()
                .todoId(todoId)
                .userId(userId)
                .emotion(emotion)
                .content(content)
                .filePath(filePath)
                .build();

        // 투두 결과 제출시 이벤트 발생

        return todoResultRepository.create(result).getId();
    }

    private UUID updateTodoResult(TodoResult todoResult, UUID userId, Emotion emotion,
            String content, MultipartFile file) {
        todoResult.validateOwner(userId);

        String filePath = todoResult.getFilePath();
        if (file != null && !file.isEmpty()) {
            String newFilePath = String.format("todo/%s/%s", todoResult.getTodoId(),
                    UUID.randomUUID());
            storageService.store(file, newFilePath);
            Events.publishEvent(new FileRollbackEvent(newFilePath));
            if (filePath != null && !filePath.isBlank()) {
                Events.publishEvent(new FileDeletedEvent(filePath));
            }
            filePath = newFilePath;
        }

        todoResult.update(emotion, content, filePath);
        return todoResultRepository.update(todoResult).getId();
    }

    private void deleteTodoResult(TodoResult todoResult) {
        if (todoResult.getFilePath() != null && !todoResult.getFilePath().isBlank()) {
            Events.publishEvent(new FileDeletedEvent(todoResult.getFilePath()));
        }
        todoResultRepository.deleteById(todoResult.getId());
    }
}
