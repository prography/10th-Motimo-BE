package kr.co.api.event.listener;

import java.util.UUID;
import kr.co.api.goal.service.GoalQueryService;
import kr.co.api.group.service.GroupCommandService;
import kr.co.api.user.service.UserQueryService;
import kr.co.domain.common.event.group.message.TodoCompletedEvent;
import kr.co.domain.common.event.group.message.TodoResultSubmittedEvent;
import kr.co.domain.goal.Goal;
import kr.co.domain.group.message.GroupMessage;
import kr.co.domain.group.message.GroupMessageType;
import kr.co.domain.group.message.MessageReference;
import kr.co.domain.group.message.MessageReferenceType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class GroupMessageEventListener {

    private final GoalQueryService goalQueryService;
    private final UserQueryService userQueryService;
    private final GroupCommandService groupCommandService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleTodoCompletedEvent(TodoCompletedEvent event) {

        Goal goal = goalQueryService.getGoalBySubGoalId(event.getSubGoalId());
        goal.validateOwner(event.getUserId());
        if (!goal.isJoinedGroup()) {
            return;
        }

        UUID groupId = goal.getGroupId();
        GroupMessage groupMessage = GroupMessage.createGroupMessage()
                .groupId(groupId)
                .userId(event.getUserId())
                .messageType(GroupMessageType.TODO_COMPLETE)
                .messageReference(
                        new MessageReference(MessageReferenceType.TODO, event.getTodoId()))
                .build();

        groupCommandService.createGroupMessage(groupMessage);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleTodoResultSubmittedEvent(TodoResultSubmittedEvent event) {

        Goal goal = goalQueryService.getGoalBySubGoalId(event.getSubGoalId());
        goal.validateOwner(event.getUserId());
        if (!goal.isJoinedGroup()) {
            return;
        }

        UUID groupId = goal.getGroupId();
        GroupMessage groupMessage = GroupMessage.createGroupMessage()
                .groupId(groupId)
                .userId(event.getUserId())
                .messageType(GroupMessageType.TODO_RESULT_SUBMIT)
                .messageReference(
                        new MessageReference(
                                MessageReferenceType.TODO_RESULT, event.getTodoResultId()))
                .build();

        groupCommandService.createGroupMessage(groupMessage);
    }
}