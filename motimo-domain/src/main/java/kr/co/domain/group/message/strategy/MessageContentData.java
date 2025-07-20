package kr.co.domain.group.message.strategy;

import java.util.Map;
import java.util.UUID;
import kr.co.domain.goal.Goal;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.TodoResult;
import kr.co.domain.user.model.User;

public record MessageContentData(
        Map<UUID, Todo> todoMap,
        Map<UUID, TodoResult> todoResultMap,
        Map<UUID, User> userMap,
        Map<UUID, Goal> goalMap
) {

    public Todo getTodo(UUID todoId) {
        return todoMap.get(todoId);
    }

    public TodoResult getTodoResult(UUID todoResultId) {
        return todoResultMap.get(todoResultId);
    }

    public User getUser(UUID userId) {
        return userMap.get(userId);
    }

    public Goal getGoal(UUID goalId) {
        return goalMap.get(goalId);
    }

}
