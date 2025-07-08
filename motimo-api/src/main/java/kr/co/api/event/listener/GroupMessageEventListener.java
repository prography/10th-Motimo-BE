package kr.co.api.event.listener;

import java.util.UUID;
import kr.co.api.event.service.OutboxCommandService;
import kr.co.api.goal.service.GoalQueryService;
import kr.co.api.group.service.GroupMessageCommandService;
import kr.co.domain.common.event.group.message.GroupJoinedEvent;
import kr.co.domain.common.event.group.message.GroupMessageDeletedEvent;
import kr.co.domain.common.event.group.message.TodoCompletedEvent;
import kr.co.domain.common.event.group.message.TodoResultSubmittedEvent;
import kr.co.domain.common.outbox.OutboxEvent;
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
    private final GroupMessageCommandService groupMessageCommandService;
    private final OutboxCommandService outboxCommandService;

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

        groupMessageCommandService.createGroupMessage(groupMessage);
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

        groupMessageCommandService.createGroupMessage(groupMessage);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleGroupJoinedEvent(GroupJoinedEvent event) {
        GroupMessage groupMessage = GroupMessage.createGroupMessage()
                .groupId(event.getGroupId())
                .userId(event.getUserId())
                .messageType(GroupMessageType.JOIN)
                .build();

        groupMessageCommandService.createGroupMessage(groupMessage);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleGroupMessageDeletedEvent(GroupMessageDeletedEvent event) {
        try {
            // 모든 그룹의 메시지들에서 해당 referenceId를 가지고 있는 메시지들을 삭제
            groupMessageCommandService.deleteAllByReferenceId(event.getReferenceId());
        } catch (Exception e) {
            log.error("그룹 메시지 삭제에 실패 메시지 관련 ID: {}", event.getReferenceId(), e);
            outboxCommandService.createOutboxEvent(OutboxEvent.from(event));
        }
    }
}