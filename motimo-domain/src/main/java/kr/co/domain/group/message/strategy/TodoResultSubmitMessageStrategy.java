package kr.co.domain.group.message.strategy;

import java.util.UUID;
import kr.co.domain.group.message.GroupMessage;
import kr.co.domain.group.message.content.GroupMessageContent;
import kr.co.domain.group.message.GroupMessageType;
import kr.co.domain.group.message.content.TodoResultSubmittedContent;
import kr.co.domain.todo.Todo;
import kr.co.domain.todo.TodoResult;
import org.springframework.stereotype.Component;

@Component
public class TodoResultSubmitMessageStrategy implements MessageContentStrategy {

    @Override
    public GroupMessageContent createContent(GroupMessage message,
            MessageContentData contentData) {
        UUID todoResultId = message.getMessageReference().referenceId();
        TodoResult todoResult = contentData.getTodoResult(todoResultId);

        if (todoResult == null) {
            return null;
        }

        Todo todo = contentData.getTodo(todoResult.getTodoId());

        if (todo == null) {
            return null;
        }

        return new TodoResultSubmittedContent(todo.getId(), todo.getTitle(), todoResult.getId(),
                todoResult.getEmotion(), todoResult.getContent(), todoResult.getFilePath());
    }

    @Override
    public GroupMessageType getMessageType() {
        return GroupMessageType.TODO_RESULT_SUBMIT;
    }
}
