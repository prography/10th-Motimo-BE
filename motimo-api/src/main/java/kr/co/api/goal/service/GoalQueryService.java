package kr.co.api.goal.service;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import kr.co.api.goal.dto.GoalDetailDto;
import kr.co.api.goal.dto.GoalItemDto;
import kr.co.api.goal.dto.GoalNotInGroupDto;
import kr.co.api.goal.dto.GoalWithSubGoalTodoDto;
import kr.co.api.goal.dto.SubGoalDto;
import kr.co.api.todo.service.TodoQueryService;
import kr.co.domain.goal.Goal;
import kr.co.domain.goal.repository.GoalRepository;
import kr.co.domain.subGoal.SubGoal;
import kr.co.domain.todo.dto.TodoSummary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoalQueryService {

    private final GoalRepository goalRepository;
    private final TodoQueryService todoQueryService;

    public GoalDetailDto getGoalDetail(UUID goalId) {
        Goal goal = goalRepository.findById(goalId);

        return new GoalDetailDto(
                goal.getId(),
                goal.getTitle(),
                goal.getDueDateValue(),
                goal.calculateProgress(),
                goal.getCreatedAt(),
                goal.completed,
                new Random().nextBoolean() // TODO: 그룹 기능 구현 시 수정
        );
    }

    public List<GoalItemDto> getGoalList(UUID userId) {
        List<Goal> goals = goalRepository.findAllByUserId(userId);
        return goals.stream().map(GoalItemDto::from).toList();
    }

    public GoalWithSubGoalTodoDto getGoalWithIncompleteSubGoalTodayTodos(UUID goalId) {
        Goal goal = goalRepository.findById(goalId);
        List<SubGoalDto> subGoals = getTodoByIncompleteSubGoalList(goal.getSubGoals());
        return GoalWithSubGoalTodoDto.of(goal, subGoals);
    }

    private List<SubGoalDto> getTodoByIncompleteSubGoalList(List<SubGoal> subGoals) {
        return subGoals.stream()
                .sorted(Comparator.comparing(SubGoal::getOrder))
                .map(subGoal -> {
                    List<TodoSummary> todos = todoQueryService.getIncompleteOrTodayTodosBySubGoalId(
                            subGoal.getId());

                    return new SubGoalDto(subGoal.getId(), subGoal.getTitle(), todos);
                })
                .toList();
    }

    public List<GoalNotInGroupDto> getGoalNotInGroup(UUID userId) {
        List<Goal> goals = goalRepository.findUnassignedGroupGoalsByUserId(userId);
        return goals.stream().map(GoalNotInGroupDto::from).toList();
    }

    public Goal getGoalBySubGoalId(UUID subGoalId) {
        return goalRepository.findBySubGoalId(subGoalId);
    }
}
