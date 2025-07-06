package kr.co.api.group.service.message;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.co.domain.group.exception.UnsupportedGroupMessageTypeException;
import kr.co.domain.group.message.GroupMessageType;
import kr.co.domain.group.message.strategy.MessageContentStrategy;
import org.springframework.stereotype.Component;

@Component
public class MessageContentStrategyFactory {

    private final Map<GroupMessageType, MessageContentStrategy> strategies;

    public MessageContentStrategyFactory(List<MessageContentStrategy> strategies) {
        this.strategies = strategies.stream()
                .collect(Collectors.toMap(
                        MessageContentStrategy::getMessageType,
                        strategy -> strategy
                ));
    }

    public MessageContentStrategy getStrategy(GroupMessageType messageType) {
        MessageContentStrategy strategy = strategies.get(messageType);
        if (strategy == null) {
            throw new UnsupportedGroupMessageTypeException();
        }
        return strategy;
    }
}
