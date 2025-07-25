package kr.co.api.goal.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import kr.co.api.goal.dto.CompletedGoalItemDto;
import kr.co.api.goal.dto.GoalDetailDto;
import kr.co.api.goal.dto.GoalItemDto;
import kr.co.api.goal.dto.GoalNotInGroupDto;
import kr.co.api.goal.dto.GoalWithSubGoalDto;
import kr.co.api.goal.dto.GoalWithSubGoalTodoDto;
import kr.co.api.goal.dto.SubGoalDto;
import kr.co.api.goal.dto.SubGoalWithTodosDto;
import kr.co.api.todo.service.TodoQueryService;
import kr.co.domain.goal.Goal;
import kr.co.domain.goal.dto.GoalTodoCount;
import kr.co.domain.goal.repository.GoalRepository;
import kr.co.domain.group.Group;
import kr.co.domain.group.repository.GroupRepository;
import kr.co.domain.subGoal.SubGoal;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.dto.TodoItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoalQueryService {

    private final GoalRepository goalRepository;
    private final GroupRepository groupRepository;
    private final TodoQueryService todoQueryService;

    public GoalDetailDto getGoalDetail(UUID goalId) {
        Goal goal = goalRepository.findByIdWithoutSubGoals(goalId);

        Optional<Group> group = groupRepository.findByGoalId(goalId);

        List<Todo> allTodos = todoQueryService.getTodosByGoalId(goalId);

        return new GoalDetailDto(
                goal.getId(),
                goal.getTitle(),
                goal.getDueDate().getMonth(),
                goal.getDueDateValue(),
                goal.calculateProgress(allTodos),
                goal.getCreatedAt(),
                goal.completed,
                group.isPresent(),
                group.map(Group::getId).orElse(null)
        );
    }

    public List<GoalItemDto> getGoalList(UUID userId) {
        List<Goal> goals = goalRepository.findAllByUserId(userId);
        return goals.stream().map(goal -> {
            List<Todo> todos = todoQueryService.getTodosByGoalId(goal.getId());
            return GoalItemDto.of(
                    goal, goal.calculateProgress(todos)
            );
        }).toList();
    }

    public GoalWithSubGoalDto getGoalWithSubGoal(UUID goalId) {
        Goal goal = goalRepository.findById(goalId);
        List<SubGoalDto> subGoals = getSubGoals(goal);
        return GoalWithSubGoalDto.of(goal, subGoals);
    }


    public List<GoalNotInGroupDto> getGoalNotInGroup(UUID userId) {
        List<Goal> goals = goalRepository.findUnassignedGroupGoalsByUserId(userId);
        return goals.stream().map(GoalNotInGroupDto::from).toList();
    }

    public List<CompletedGoalItemDto> getCompletedGoalsByUserId(UUID userId) {
        List<Goal> completedGoals = goalRepository.findCompletedGoalsByUserId(userId);

        if (completedGoals.isEmpty()) {
            return List.of();
        }

        Map<UUID, GoalTodoCount> countMap = createGoalTodoCountMap(completedGoals);

        return completedGoals.stream()
                .map(goal -> {
                    GoalTodoCount tc = countMap.getOrDefault(
                            goal.getId(), new GoalTodoCount(goal.getId(), 0, 0));
                    return CompletedGoalItemDto.of(goal, tc.todoCount(), tc.todoResultCount());
                })
                .toList();
    }

    public Goal getGoalBySubGoalId(UUID subGoalId) {
        return goalRepository.findBySubGoalId(subGoalId);
    }

    public List<Goal> getGoalsByUserId(UUID userId) {
        return goalRepository.findAllByUserId(userId);
    }

    public GoalWithSubGoalTodoDto getGoalWithIncompleteSubGoalTodayTodos(UUID goalId) {
        Goal goal = goalRepository.findById(goalId);
        List<SubGoalWithTodosDto> subGoals = getTodoByIncompleteSubGoalList(goal.getSubGoals());
        return GoalWithSubGoalTodoDto.of(goal, subGoals);
    }

    public GoalWithSubGoalTodoDto getGoalWithSubGoalTodos(UUID goalId) {
        Goal goal = goalRepository.findById(goalId);
        List<SubGoalWithTodosDto> subGoals = getTodoBySubGoalList(goal.getSubGoals());
        return GoalWithSubGoalTodoDto.of(goal, subGoals);
    }

    private List<SubGoalWithTodosDto> getTodoByIncompleteSubGoalList(List<SubGoal> subGoals) {
        return subGoals.stream()
                .sorted(Comparator.comparing(SubGoal::getOrder))
                .map(subGoal -> {
                    List<TodoItemDto> todos = todoQueryService.getIncompleteOrTodayTodosBySubGoalId(
                            subGoal.getId());

                    return new SubGoalWithTodosDto(subGoal.getId(), subGoal.getTitle(),
                            subGoal.isCompleted(), todos);
                })
                .toList();
    }

    private List<SubGoalWithTodosDto> getTodoBySubGoalList(List<SubGoal> subGoals) {
        return subGoals.stream()
                .sorted(Comparator.comparing(SubGoal::getOrder))
                .map(subGoal -> {
                    List<TodoItemDto> todos = todoQueryService.getTodosBySubGoalId(
                            subGoal.getId());
                    return new SubGoalWithTodosDto(subGoal.getId(), subGoal.getTitle(),
                            subGoal.isCompleted(), todos);
                })
                .toList();
    }

    private List<SubGoalDto> getSubGoals(Goal goal) {
        return goal.getSubGoals().stream()
                .sorted(Comparator.comparing(SubGoal::getOrder))
                .map(subGoal -> new SubGoalDto(subGoal.getId(), subGoal.getTitle(),
                        subGoal.isCompleted()))
                .toList();
    }

    private List<UUID> extractGoalIds(List<Goal> goals) {
        return goals.stream().map(Goal::getId).toList();
    }

    private Map<UUID, GoalTodoCount> createGoalTodoCountMap(List<Goal> goals) {
        List<UUID> goalIds = extractGoalIds(goals);
        return todoQueryService.getTodoCountsByGoalIds(goalIds)
                .stream()
                .collect(Collectors.toMap(GoalTodoCount::goalId, Function.identity()));
    }

}
