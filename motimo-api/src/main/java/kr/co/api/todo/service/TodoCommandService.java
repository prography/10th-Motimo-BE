package kr.co.api.todo.service;

import java.time.LocalDate;
import java.util.UUID;
import kr.co.domain.common.event.Events;
import kr.co.domain.common.event.FileDeletedEvent;
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

    public Todo createTodo(UUID userId, UUID subGoalId, String title, LocalDate date) {
        Todo todo = Todo.createTodo()
                .userId(userId)
                .subGoalId(subGoalId)
                .title(title)
                .date(date)
                .build();
        return todoRepository.save(todo);
    }

    public TodoResult submitTodoResult(UUID userId, UUID todoId, Emotion emotion, String content,
            MultipartFile file) {
        Todo todo = todoRepository.findById(todoId);
        todo.validateOwner(userId);

        String filePath = "";
        if (file != null && !file.isEmpty()) {
            filePath = String.format("todo/%s/%s", todoId, UUID.randomUUID());
            storageService.store(file, filePath);
            // 이미지 삭제 이벤트 발행 (트랜잭션 롤백 시 동작)
            Events.publishEvent(new FileDeletedEvent(filePath));
        }

        TodoResult result = TodoResult.createTodoResult()
                .todoId(todoId)
                .userId(userId)
                .emotion(emotion)
                .content(content)
                .filePath(filePath)
                .build();

        return todoResultRepository.save(result);
    }

    public Todo toggleTodoCompletion(UUID userId, UUID todoId) {
        Todo todo = todoRepository.findById(todoId);
        todo.validateOwner(userId);
        todo.toggleCompletion();
        return todoRepository.save(todo);
    }

    public Todo updateTodo(UUID userId, UUID todoId, String title, LocalDate date) {
        Todo todo = todoRepository.findById(todoId);
        todo.validateOwner(userId);
        todo.update(title, date);
        return todoRepository.save(todo);
    }

    public void deleteById(UUID userId, UUID todoId) {
        Todo todo = todoRepository.findById(todoId);
        todo.validateOwner(userId);
        todoRepository.deleteById(todoId);
    }
}
