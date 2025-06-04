package kr.co.api.todo.service;

import java.util.Optional;
import java.util.UUID;
import kr.co.domain.common.pagination.CustomSlice;
import kr.co.domain.todo.TodoResult;
import kr.co.domain.todo.dto.TodoSummary;
import kr.co.domain.todo.exception.TodoNotFoundException;
import kr.co.domain.todo.repository.TodoRepository;
import kr.co.domain.todo.repository.TodoResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TodoQueryService {

    private final TodoRepository todoRepository;
    private final TodoResultRepository todoResultRepository;

    public CustomSlice<TodoSummary> getTodosBySubGoal(UUID subGoalId, int page, int size) {
        return todoRepository.findAllBySubGoalId(subGoalId, page, size);
    }

    public CustomSlice<TodoSummary> getTodosByUser(UUID userId, int page, int size) {
        return todoRepository.findAllByUserId(userId, page, size);
    }

    public Optional<TodoResult> getTodoResultByTodoId(UUID todoId) {
        if (!todoRepository.existsById(todoId)) {
            throw new TodoNotFoundException();
        }
        return todoResultRepository.findByTodoId(todoId);
    }
}
