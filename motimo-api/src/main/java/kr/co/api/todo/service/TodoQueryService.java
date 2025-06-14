package kr.co.api.todo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import kr.co.api.todo.rqrs.TodoResultRs;
import kr.co.domain.todo.dto.TodoSummary;
import kr.co.domain.todo.exception.TodoNotFoundException;
import kr.co.domain.todo.repository.TodoRepository;
import kr.co.domain.todo.repository.TodoResultRepository;
import kr.co.infra.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TodoQueryService {

    private final TodoRepository todoRepository;
    private final TodoResultRepository todoResultRepository;
    private final StorageService storageService;

    public List<TodoSummary> getIncompleteOrTodayTodosBySubGoalId(UUID subGoalId) {
        return todoRepository.findIncompleteOrDateTodosBySubGoalId(subGoalId, LocalDate.now());
    }

    public List<TodoSummary> getTodosByUserId(UUID userId) {
        return todoRepository.findAllByUserId(userId);
    }

    public Optional<TodoResultRs> getTodoResultByTodoId(UUID todoId) {
        if (!todoRepository.existsById(todoId)) {
            throw new TodoNotFoundException();
        }
        return todoResultRepository.findByTodoId(todoId)
                .map(result ->
                        TodoResultRs.of(result, storageService.getFileUrl(result.getFilePath())));
    }
}
