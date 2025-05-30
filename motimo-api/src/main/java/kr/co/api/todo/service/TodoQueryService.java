package kr.co.api.todo.service;

import kr.co.domain.common.pagination.CustomSlice;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TodoQueryService {

    private final TodoRepository todoRepository;

    public Todo getTodo(UUID id) {
        return todoRepository.findById(id);
    }

    public CustomSlice<Todo> getTodosBySubGoal(UUID subGoalId, int page, int size) {
        return todoRepository.findAllBySubGoalId(subGoalId, page, size);
    }

    public CustomSlice<Todo> getTodosByUser(UUID userId, int page, int size) {
        return todoRepository.findAllByUserId(userId, page, size);
    }
}
