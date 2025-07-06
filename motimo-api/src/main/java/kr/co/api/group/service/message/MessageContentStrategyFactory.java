package kr.co.api.group.service.message;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kr.co.domain.group.exception.UnsupportedGroupMessageTypeException;
import kr.co.domain.group.message.GroupMessageType;
import kr.co.domain.group.message.strategy.MessageContentStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageContentStrategyFactory {

    private final Map<GroupMessageType, MessageContentStrategy> strategies;

    public MessageContentStrategyFactory(List<MessageContentStrategy> strategies) {
        this.strategies = strategies.stream()
                .collect(Collectors.toMap(
                        MessageContentStrategy::getMessageType,
                        strategy -> strategy,
                        (existing, replacement) -> {
                            log.warn("메시지 타입이 중복되었습니다: {}. 이미 사용중인 전략을 사용합니다.",
                                    existing.getMessageType());
                            return existing;
                        }
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
