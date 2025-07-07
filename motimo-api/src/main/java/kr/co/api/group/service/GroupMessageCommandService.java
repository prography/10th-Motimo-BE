package kr.co.api.group.service;

import java.util.UUID;
import kr.co.domain.group.message.GroupMessage;
import kr.co.domain.group.message.repository.GroupMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupMessageCommandService {

    private final GroupMessageRepository groupMessageRepository;

    public void createGroupMessage(GroupMessage groupMessage) {
        groupMessageRepository.create(groupMessage);
    }

    public void deleteAllByReferenceId(UUID referenceId) {
        groupMessageRepository.deleteAllByReferenceId(referenceId);
    }
}
