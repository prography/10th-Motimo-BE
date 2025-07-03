package kr.co.infra.rdb.group;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import kr.co.domain.goal.DueDate;
import kr.co.domain.group.Group;
import kr.co.domain.group.repository.GroupRepository;
import kr.co.infra.rdb.config.QueryDslConfig;
import kr.co.infra.rdb.goal.entity.DueDateEmbeddable;
import kr.co.infra.rdb.goal.entity.GoalEntity;
import kr.co.infra.rdb.group.entity.GroupEntity;
import kr.co.infra.rdb.group.entity.GroupMemberEntity;
import kr.co.infra.rdb.group.repository.GroupRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
@Import({GroupRepositoryImpl.class, QueryDslConfig.class})
@EntityScan(basePackages = "kr.co.infra.rdb")
class GroupRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GroupRepository groupRepository;

    private UUID testUserId;
    private UUID otherUserId;
    private LocalDate testDueDate;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        otherUserId = UUID.randomUUID();
        testDueDate = LocalDate.of(2024, 6, 15); // Day of year: 167
    }

    @Test
    @DisplayName("사용 가능한 그룹이 존재하지 않을 때 빈 Optional 반환")
    void shouldReturnEmptyWhenNoAvailableGroup() {
        // Given
        LocalDate searchDueDate = LocalDate.of(2024, 6, 15);

        // When
        Optional<Group> result = groupRepository.findAvailableGroupBySimilarDueDate(testUserId, searchDueDate);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("멤버 수가 6명 미만이고 날짜 차이가 30일 이내인 그룹 찾기 성공")
    void shouldFindAvailableGroupWithSimilarDueDate() {
        // Given
        GroupEntity group = createGroup();
        entityManager.persistAndFlush(group);

        // 5명의 멤버 추가 (MAX_GROUP_COUNT 미만)
        for (int i = 0; i < 5; i++) {
            UUID memberId = UUID.randomUUID();

            // 목표 완료날짜를 비슷하게 설정 (6월 10일 ~ 6월 20일)
            LocalDate memberDueDate = LocalDate.of(2024, 6, 10 + i * 2);

            GoalEntity goal = entityManager.persistAndFlush(createGoal(memberDueDate));
            GroupMemberEntity member = createGroupMember(group, memberId, goal.getId());

            entityManager.persistAndFlush(member);
        }

        LocalDate searchDueDate = LocalDate.of(2024, 6, 15);

        // When
        Optional<Group> result = groupRepository.findAvailableGroupBySimilarDueDate(testUserId, searchDueDate);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(group.getId());
    }

    @Test
    @DisplayName("멤버 수가 6명 이상인 그룹은 제외")
    void shouldExcludeGroupWithMaxMembers() {
        // Given
        GroupEntity group = createGroup();
        entityManager.persistAndFlush(group);

        // 6명의 멤버 추가 (MAX_GROUP_COUNT 이상)
        for (int i = 0; i < 6; i++) {
            UUID memberId = UUID.randomUUID();
            UUID goalId = UUID.randomUUID();

            LocalDate memberDueDate = LocalDate.of(2024, 6, 15);

            GoalEntity goal = entityManager.persistAndFlush(createGoal(memberDueDate));
            GroupMemberEntity member = createGroupMember(group, memberId, goal.getId());

            entityManager.persistAndFlush(member);
        }

        LocalDate searchDueDate = LocalDate.of(2024, 6, 15);

        // When
        Optional<Group> result = groupRepository.findAvailableGroupBySimilarDueDate(testUserId, searchDueDate);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("사용자가 이미 속한 그룹은 제외")
    void shouldExcludeGroupUserAlreadyJoined() {
        // Given
        GroupEntity group = createGroup();
        entityManager.persistAndFlush(group);

        UUID goalId = UUID.randomUUID();
        LocalDate memberDueDate = LocalDate.of(2024, 6, 15);

        GoalEntity goal = entityManager.persistAndFlush(createGoal(memberDueDate));
        GroupMemberEntity member = createGroupMember(group, testUserId, goal.getId());

        entityManager.persistAndFlush(member);

        LocalDate searchDueDate = LocalDate.of(2024, 6, 15);

        // When
        Optional<Group> result = groupRepository.findAvailableGroupBySimilarDueDate(testUserId, searchDueDate);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("평균 완료날짜와 30일 초과 차이나는 그룹은 제외")
    void shouldExcludeGroupWithFarDueDate() {
        // Given
        GroupEntity group = createGroup();
        entityManager.persistAndFlush(group);

        // 3명의 멤버 추가 (완료날짜를 8월로 설정)
        for (int i = 0; i < 3; i++) {
            UUID memberId = UUID.randomUUID();
            UUID goalId = UUID.randomUUID();

            LocalDate memberDueDate = LocalDate.of(2024, 8, 15); // Day of year: 228

            GoalEntity goal = entityManager.persistAndFlush(createGoal(memberDueDate));
            GroupMemberEntity member = createGroupMember(group, memberId, goal.getId());

            entityManager.persistAndFlush(member);
        }

        LocalDate searchDueDate = LocalDate.of(2024, 6, 15); // Day of year: 167, 차이: 61일

        // When
        Optional<Group> result = groupRepository.findAvailableGroupBySimilarDueDate(testUserId, searchDueDate);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("여러 조건을 만족하는 그룹 중 첫 번째 그룹 반환")
    void shouldReturnFirstAvailableGroup() {
        // Given
        GroupEntity group1 = createGroup();
        GroupEntity group2 = createGroup();
        entityManager.persistAndFlush(group1);
        entityManager.persistAndFlush(group2);

        // 두 그룹 모두 조건을 만족하도록 설정
        setupGroupWithMembers(group1, 3);
        setupGroupWithMembers(group2, 4);

        LocalDate searchDueDate = LocalDate.of(2024, 6, 15);

        // When
        Optional<Group> result = groupRepository.findAvailableGroupBySimilarDueDate(testUserId, searchDueDate);

        // Then
        assertThat(result).isPresent();
        // limit(1)로 인해 첫 번째 그룹이 반환되어야 함
    }

    @Test
    @DisplayName("경계값 테스트: 정확히 30일 차이나는 경우")
    void shouldIncludeGroupWithExactly30DaysDifference() {
        // Given
        GroupEntity group = createGroup();
        entityManager.persistAndFlush(group);

        // 평균 완료날짜가 정확히 30일 차이나도록 설정
        UUID memberId = UUID.randomUUID();
        UUID goalId = UUID.randomUUID();

        LocalDate memberDueDate = LocalDate.of(2024, 7, 15); // Day of year: 197

        GoalEntity goal = entityManager.persistAndFlush(createGoal(memberDueDate));
        GroupMemberEntity member = createGroupMember(group, memberId, goal.getId());

        entityManager.persistAndFlush(member);

        LocalDate searchDueDate = LocalDate.of(2024, 6, 15); // Day of year: 167, 차이: 30일

        // When
        Optional<Group> result = groupRepository.findAvailableGroupBySimilarDueDate(testUserId, searchDueDate);

        // Then
        assertThat(result).isPresent();
    }

    // Helper methods
    private GroupEntity createGroup() {
        return new GroupEntity();
    }

    private GoalEntity createGoal(LocalDate dueDate) {
        return new GoalEntity(
                null,
                UUID.randomUUID(),
                "목표 제목",
                DueDateEmbeddable.from(DueDate.of(dueDate)),
                false
        );
    }

    private GroupMemberEntity createGroupMember(GroupEntity group, UUID userId, UUID goalId) {
        return new GroupMemberEntity(
                null,
                userId,
                goalId,
                group,
                LocalDateTime.now()
        );
    }

    private void setupGroupWithMembers(GroupEntity group, int memberCount) {
        for (int i = 0; i < memberCount; i++) {
            UUID memberId = UUID.randomUUID();

            LocalDate memberDueDate = LocalDate.of(2024, 6, 15);

            GoalEntity goal = entityManager.persistAndFlush(createGoal(memberDueDate));
            GroupMemberEntity member = createGroupMember(group, memberId, goal.getId());

            entityManager.persistAndFlush(member);
        }
    }
}

