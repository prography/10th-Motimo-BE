package kr.co.domain.group.message.strategy;

import java.util.UUID;
import kr.co.domain.group.message.GroupMessage;
import kr.co.domain.group.message.content.GroupMessageContent;
import kr.co.domain.group.message.GroupMessageType;
import kr.co.domain.group.message.content.TodoCompletedContent;
import kr.co.domain.todo.Todo;
import org.springframework.stereotype.Component;

@Component
public class TodoCompleteMessageStrategy implements MessageContentStrategy {

    @Override
    public GroupMessageContent createContent(GroupMessage message, MessageContentData contentData) {
        UUID todoId = message.getMessageReference().referenceId();
        Todo todo = contentData.getTodo(todoId);

        if (todo == null) {
            return null;
        }

        return new TodoCompletedContent(todoId, todo.getTitle());
    }

    @Override
    public GroupMessageType getMessageType() {
        return GroupMessageType.TODO_COMPLETE;
    }
}
