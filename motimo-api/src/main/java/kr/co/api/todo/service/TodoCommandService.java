package kr.co.api.todo.service;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;
import kr.co.domain.common.event.Events;
import kr.co.domain.common.event.FileDeletedEvent;
import kr.co.domain.common.event.FileRollbackEvent;
import kr.co.domain.common.event.group.message.GroupMessageDeletedEvent;
import kr.co.domain.common.event.group.message.TodoCompletedEvent;
import kr.co.domain.common.event.group.message.TodoResultSubmittedEvent;
import kr.co.domain.todo.Emotion;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.TodoResult;
import kr.co.domain.todo.exception.TodoNotCompleteException;
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
        if (!todo.isComplete()) {
            throw new TodoNotCompleteException();
        }

        return todoResultRepository.findByTodoId(todoId)
                .map(todoResult -> updateTodoResult(todoResult, userId, emotion, content, file))
                .orElseGet(() -> createTodoResult(userId, todo, emotion, content, file));
    }

    public UUID toggleTodoCompletion(UUID userId, UUID todoId) {
        Todo todo = todoRepository.findById(todoId);
        todo.validateOwner(userId);
        todo.toggleCompletion();

        if (todo.isComplete()) {
            Events.publishEvent(
                    new TodoCompletedEvent(userId, todo.getSubGoalId(), todo.getId(),
                            todo.getTitle()));
        }
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
        Events.publishEvent(new GroupMessageDeletedEvent(todoId));
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

    public void deleteAllTodoAndResultBySubGoalIds(Set<UUID> subGoalIds) {
        todoRepository.deleteAllTodoCascadeBySubGoalIds(subGoalIds);
    }

    private UUID createTodoResult(UUID userId, Todo todo, Emotion emotion, String content,
            MultipartFile file) {
        String filePath = "";
        if (file != null && !file.isEmpty()) {
            filePath = String.format("todo/%s/%s", todo.getId(), UUID.randomUUID());
            storageService.store(file, filePath);
            Events.publishEvent(new FileRollbackEvent(filePath));
        }

        TodoResult result = TodoResult.createTodoResult()
                .todoId(todo.getId())
                .userId(userId)
                .emotion(emotion)
                .content(content)
                .filePath(filePath)
                .build();

        TodoResult todoResult = todoResultRepository.create(result);
        Events.publishEvent(TodoResultSubmittedEvent.of(userId, todo, todoResult));
        return todoResult.getId();
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
        Events.publishEvent(new GroupMessageDeletedEvent(todoResult.getId()));
        todoResultRepository.deleteById(todoResult.getId());
    }
}
