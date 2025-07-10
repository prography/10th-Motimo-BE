package kr.co.api.todo.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import kr.co.domain.goal.dto.GoalTodoCount;
import kr.co.domain.todo.TodoResult;
import kr.co.domain.todo.TodoStatus;
import kr.co.domain.todo.dto.TodoItem;
import kr.co.domain.todo.dto.TodoResultItem;
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

    public List<TodoItem> getIncompleteOrTodayTodosBySubGoalId(UUID subGoalId) {
        LocalDate today = LocalDate.now();

        return todoRepository.findIncompleteOrDateTodosBySubGoalId(subGoalId, today).stream()
                .sorted(todoPriorityComparator())
                .map(this::enrichTodoItemWithUrl)
                .toList();
    }

    public List<TodoItem> getTodosBySubGoalId(UUID subGoalId) {
        return todoRepository.findAllBySubGoalId(subGoalId).stream()
                .sorted(todoPriorityComparator())
                .map(this::enrichTodoItemWithUrl)
                .toList();
    }

    public List<TodoItem> getTodosByUserId(UUID userId) {
        return todoRepository.findAllByUserId(userId).stream()
                .sorted(todoPriorityComparator())
                .map(this::enrichTodoItemWithUrl)
                .toList();
    }

    public Optional<TodoResultItem> getTodoResultByTodoId(UUID todoId) {
        validateTodoExists(todoId);
        return todoResultRepository.findByTodoId(todoId).map(this::toTodoResultItemWithFileUrl);
    }

    public List<GoalTodoCount> getTodoCountsByGoalIds(List<UUID> goalIds) {
        return todoRepository.countTodosByGoalIds(goalIds);
    }

    private void validateTodoExists(UUID todoId) {
        if (!todoRepository.existsById(todoId)) {
            throw new TodoNotFoundException();
        }
    }

    private TodoItem enrichTodoItemWithUrl(TodoItem todoItem) {
        TodoResultItem todoResultItem = todoItem.todoResultItem();

        if (todoResultItem == null || todoResultItem.id() == null) {
            return todoItem.withTodoResultItem(null);
        }

        if (todoResultItem.fileUrl() == null) {
            return todoItem;
        }
        String url = storageService.getFileUrl(todoResultItem.fileUrl());
        return todoItem.withTodoResultItem(todoResultItem.withFileUrl(url));
    }

    private TodoResultItem toTodoResultItemWithFileUrl(TodoResult result) {
        if (result.getFilePath() == null) {
            return TodoResultItem.of(result, null);
        }
        String fileUrl = storageService.getFileUrl(result.getFilePath());
        return TodoResultItem.of(result, fileUrl);
    }

    private Comparator<TodoItem> todoPriorityComparator() {
        return Comparator
                .comparingInt(this::rankTodoByCompletionAndSubmission)
                .thenComparing(TodoItem::date, Comparator.nullsLast(LocalDate::compareTo))
                .thenComparing(TodoItem::createdAt);
    }

    private int rankTodoByCompletionAndSubmission(TodoItem todo) {
        if (todo.status() == TodoStatus.INCOMPLETE) {
            return 0;
        }
        if (todo.status() == TodoStatus.COMPLETE && todo.todoResultItem() == null) {
            return 1;
        }
        return 2;
    }
}
