package kr.co.api.todo.service;

import java.time.LocalDate;
import java.util.UUID;
import kr.co.domain.common.event.Events;
import kr.co.domain.common.event.ImageDeletedEvent;
import kr.co.domain.todo.Emotion;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.TodoResult;
import kr.co.domain.todo.repository.TodoRepository;
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
    private final StorageService storageService;

    public void createTodo(UUID userId, UUID subGoalId, String title, LocalDate date) {
        Todo todo = Todo.builder()
                .authorId(userId)
                .subGoalId(subGoalId)
                .title(title)
                .date(date)
                .build();
        todoRepository.save(todo);
    }

    public void submitTodoResult(UUID userId, UUID id, Emotion emotion, String content,
            MultipartFile image) {
        Todo todo = todoRepository.findById(id);
        todo.validateAuthor(userId);

        String imageName = "";
        if (image != null && !image.isEmpty()) {
            imageName = String.format("todo/%s/%s", id, UUID.randomUUID());
            storageService.uploadImage(image, imageName);
            // 이미지 삭제 이벤트 발행 (트랜잭션 롤백 시 동작)
            Events.publishEvent(new ImageDeletedEvent(imageName));
        }

        TodoResult result = TodoResult.builder()
                .emotion(emotion)
                .content(content)
                .imageName(imageName)
                .build();

        todo.complete(result);
        todoRepository.save(todo);
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
