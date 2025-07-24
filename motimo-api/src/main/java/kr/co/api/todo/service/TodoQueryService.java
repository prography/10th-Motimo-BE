package kr.co.api.todo.service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import kr.co.domain.common.pagination.CustomSlice;
import kr.co.domain.goal.dto.GoalTodoCount;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.TodoResult;
import kr.co.domain.todo.TodoStatus;
import kr.co.domain.todo.dto.TodoItemDto;
import kr.co.domain.todo.dto.TodoResultItemDto;
import kr.co.domain.todo.exception.TodoNotFoundException;
import kr.co.domain.todo.repository.TodoRepository;
import kr.co.domain.todo.repository.TodoResultRepository;
import kr.co.infra.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TodoQueryService {

    private final TodoRepository todoRepository;
    private final TodoResultRepository todoResultRepository;
    private final StorageService storageService;

    public List<TodoItemDto> getIncompleteOrTodayTodosBySubGoalId(UUID subGoalId) {
        LocalDate today = LocalDate.now();
        return todoRepository.findAllIncompleteOrDateTodoBySubGoalId(subGoalId, today)
                .stream()
                .map(this::enrichTodoItemWithUrl)
                .sorted(todoPriorityComparator())
                .toList();
    }

    public CustomSlice<TodoItemDto> getIncompleteOrTodayTodosBySubGoalIdWithSlice(UUID subGoalId,
            int offset, int size) {
        LocalDate today = LocalDate.now();

        CustomSlice<TodoItemDto> todos = todoRepository.findIncompleteOrDateTodosBySubGoalId(
                subGoalId, today, offset, size);
        List<TodoItemDto> result = todos.content().stream()
                .map(this::enrichTodoItemWithUrl)
                .sorted(todoPriorityComparator())
                .toList();

        return new CustomSlice<>(result, todos.hasNext(), todos.offset(), todos.size());
    }

    public CustomSlice<TodoItemDto> getTodosBySubGoalIdWithSlice(UUID subGoalId, int offset,
            int size) {

        CustomSlice<TodoItemDto> todos = todoRepository.findAllBySubGoalIdWithSlice(subGoalId,
                offset, size);

        List<TodoItemDto> result = todos.content().stream()
                .map(this::enrichTodoItemWithUrl)
                .sorted(todoPriorityComparator())
                .toList();

        return new CustomSlice<>(result, todos.hasNext(), todos.offset(), todos.size());
    }

    public List<TodoItemDto> getTodosByUserId(UUID userId) {
        return todoRepository.findAllByUserId(userId).stream()
                .sorted(todoPriorityComparator())
                .map(this::enrichTodoItemWithUrl)
                .toList();
    }

    public List<Todo> getTodosByGoalId(UUID goalId) {
        return todoRepository.findAllByGoalId(goalId);
    }

    public Optional<TodoResultItemDto> getTodoResultByTodoId(UUID todoId) {
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

    private TodoItemDto enrichTodoItemWithUrl(TodoItemDto todoItem) {
        TodoResultItemDto todoResultItem = todoItem.todoResultItem();

        if (todoResultItem == null || todoResultItem.id() == null) {
            return todoItem.withTodoResultItem(null);
        }

        if (!StringUtils.hasText(todoResultItem.fileUrl())) {
            return todoItem;
        }
        String url = storageService.getFileUrl(todoResultItem.fileUrl());
        return todoItem.withTodoResultItem(
                todoResultItem.withFileUrl(url, todoResultItem.fileName()));
    }

    private TodoResultItemDto toTodoResultItemWithFileUrl(TodoResult result) {
        if (!StringUtils.hasText(result.getFilePath())) {
            return TodoResultItemDto.of(result, null, result.getFileName());
        }
        String fileUrl = storageService.getFileUrl(result.getFilePath());
        return TodoResultItemDto.of(result, fileUrl, result.getFileName());
    }

    private Comparator<TodoItemDto> todoPriorityComparator() {
        return Comparator
                .comparingInt(this::rankTodoByCompletionAndSubmission)
                .thenComparing(TodoItemDto::date, Comparator.nullsLast(LocalDate::compareTo))
                .thenComparing(TodoItemDto::createdAt);
    }

    private int rankTodoByCompletionAndSubmission(TodoItemDto todo) {
        if (todo.status() == TodoStatus.INCOMPLETE) {
            return 0;
        }
        if (todo.status() == TodoStatus.COMPLETE && todo.todoResultItem() == null) {
            return 1;
        }
        return 2;
    }
}
