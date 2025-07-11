package kr.co.api.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Set;
import java.util.UUID;
import kr.co.domain.common.event.Events;
import kr.co.domain.common.event.FileDeletedEvent;
import kr.co.domain.common.event.FileRollbackEvent;
import kr.co.domain.user.exception.UserNotFoundException;
import kr.co.domain.user.model.InterestType;
import kr.co.domain.user.model.ProviderType;
import kr.co.domain.user.model.Role;
import kr.co.domain.user.model.User;
import kr.co.domain.user.repository.UserRepository;
import kr.co.infra.storage.exception.StorageErrorCode;
import kr.co.infra.storage.exception.StorageException;
import kr.co.infra.storage.service.StorageService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Command 테스트")
class UserCommandServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StorageService storageService;

    private MockedStatic<Events> mockedEvents;

    @InjectMocks
    private UserCommandService userCommandService;

    @BeforeEach
    void setUp() {
        mockedEvents = mockStatic(Events.class);
    }

    @AfterEach
    void close() {
        mockedEvents.close();
    }

    @Test
    void 유저_정상_생성() {
        // given
        User testUser = User.builder()
                .email("test@gmail.com")
                .nickname("테스트 사용자")
                .providerType(ProviderType.GOOGLE)
                .role(Role.USER)
                .build();

        when(userRepository.create(any(User.class))).thenReturn(testUser);

        // when
        User savedUser = userCommandService.register(testUser);

        // then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("test@gmail.com");
        assertThat(savedUser.getNickname()).isEqualTo("테스트 사용자");
        assertThat(savedUser.getProviderType()).isEqualTo(ProviderType.GOOGLE);
        assertThat(savedUser.getRole()).isEqualTo(Role.USER);

        verify(userRepository, times(1)).create(testUser);
    }

    @Test
    @DisplayName("Repository 저장 실패 시 예외 전파")
    void repository_저장_실패시_예외_발생() {
        // given
        User userToSave = User.builder()
                .email("test@gmail.com")
                .nickname("테스트 사용자")
                .providerType(ProviderType.LOCAL)
                .build();

        when(userRepository.create(any(User.class)))
                .thenThrow(new RuntimeException("데이터베이스 저장시 오류"));

        // when & then
        assertThatThrownBy(() -> userCommandService.register(userToSave))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("데이터베이스 저장시 오류");

        verify(userRepository, times(1)).create(userToSave);
    }

    @Test
    @DisplayName("유저 관심사 업데이트 성공")
    void 유저_관심사_업데이트_성공() {
        // given
        UUID userId = UUID.randomUUID();
        Set<InterestType> interests = Set.of(InterestType.PROGRAMMING, InterestType.SPORTS);

        User existingUser = User.builder()
                .id(userId)
                .email("test@gmail.com")
                .nickname("테스트 사용자")
                .providerType(ProviderType.GOOGLE)
                .role(Role.USER)
                .build();

        when(userRepository.findById(userId)).thenReturn(existingUser);
        when(userRepository.update(any(User.class))).thenReturn(existingUser);

        // when
        UUID result = userCommandService.updateInterests(userId, interests);

        // then
        assertThat(result).isEqualTo(userId);
        assertThat(existingUser.getInterests()).isEqualTo(interests);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).update(existingUser);
    }

    @Test
    @DisplayName("존재하지 않는 유저 관심사 업데이트 시 예외 발생")
    void 존재하지_않는_유저_관심사_업데이트시_예외_발생() {
        // given
        UUID nonExistentUserId = UUID.randomUUID();
        Set<InterestType> interests = Set.of(InterestType.PROGRAMMING);

        when(userRepository.findById(nonExistentUserId)).thenThrow(new UserNotFoundException());

        // when & then
        assertThatThrownBy(() -> userCommandService.updateInterests(nonExistentUserId, interests))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository, times(1)).findById(nonExistentUserId);
        verify(userRepository, never()).update(any(User.class));
    }

    @Test
    void 유저_프로필_업데이트_성공_이미지_없음() {
        // given
        UUID userId = UUID.randomUUID();
        String userName = "새로운 이름";
        String bio = "새로운 소개";
        String currentImagePath = "user/%s/%s".formatted(userId, userId);
        Set<InterestType> interests = Set.of(InterestType.PROGRAMMING, InterestType.SPORTS);

        User existingUser = User.builder()
                .id(userId)
                .email("test@gmail.com")
                .nickname("기존 이름")
                .providerType(ProviderType.GOOGLE)
                .role(Role.USER)
                .profileImagePath(currentImagePath)
                .build();

        when(userRepository.findById(userId)).thenReturn(existingUser);
        when(userRepository.update(any(User.class))).thenReturn(existingUser);

        // when
        UUID result = userCommandService.updateProfile(userId, userName, bio, interests, null);

        // then
        assertThat(result).isEqualTo(userId);
        assertThat(existingUser.getNickname()).isEqualTo(userName);
        assertThat(existingUser.getBio()).isEqualTo(bio);
        assertThat(existingUser.getInterests()).isEqualTo(interests);
        assertThat(existingUser.getProfileImagePath()).isEqualTo(currentImagePath);

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).update(existingUser);
        verify(storageService, never()).store(any(), any());
        mockedEvents.verify(() -> Events.publishEvent(any()), never());
    }

    @Test
    void 유저_프로필_업데이트_성공_빈_이미지() {
        // given
        UUID userId = UUID.randomUUID();
        String userName = "새로운 이름";
        String bio = "새로운 소개";
        Set<InterestType> interests = Set.of(InterestType.PROGRAMMING);
        MultipartFile emptyImage = mock(MultipartFile.class);

        when(emptyImage.isEmpty()).thenReturn(true);

        User existingUser = User.builder()
                .id(userId)
                .email("test@gmail.com")
                .nickname("기존 이름")
                .providerType(ProviderType.GOOGLE)
                .profileImagePath("")
                .role(Role.USER)
                .build();

        when(userRepository.findById(userId)).thenReturn(existingUser);
        when(userRepository.update(any(User.class))).thenReturn(existingUser);

        // when
        UUID result = userCommandService.updateProfile(userId, userName, bio, interests,
                emptyImage);

        // then
        assertThat(result).isEqualTo(userId);
        assertThat(existingUser.getNickname()).isEqualTo(userName);
        assertThat(existingUser.getBio()).isEqualTo(bio);
        assertThat(existingUser.getInterests()).isEqualTo(interests);
        assertThat(existingUser.getProfileImagePath()).isEqualTo("");
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).update(existingUser);
        verify(storageService, never()).store(any(), any());
        mockedEvents.verify(() -> Events.publishEvent(any()), never());
    }

    @Test
    void 유저_프로필_업데이트_성공_새_이미지_업로드_기존_이미지_있음() {
        // given
        UUID userId = UUID.randomUUID();
        String userName = "새로운 이름";
        String bio = "새로운 소개";
        Set<InterestType> interests = Set.of(InterestType.PROGRAMMING);
        MultipartFile newImage = mock(MultipartFile.class);
        String existingProfileUrl = "user/%s/%s".formatted(userId, userId);

        when(newImage.isEmpty()).thenReturn(false);

        User existingUser = User.builder()
                .id(userId)
                .email("test@gmail.com")
                .nickname("기존 이름")
                .providerType(ProviderType.GOOGLE)
                .role(Role.USER)
                .profileImagePath(existingProfileUrl)
                .build();

        when(userRepository.findById(userId)).thenReturn(existingUser);
        when(userRepository.update(any(User.class))).thenReturn(existingUser);
        doAnswer(invocation -> {
            // 실제 서비스에서 generateProfileImagePath()가 호출되어 새 경로가 생성됨을 시뮬레이션
            return null;
        }).when(storageService).store(eq(newImage), startsWith("user/" + userId + "/"));

        // when
        UUID result = userCommandService.updateProfile(userId, userName, bio, interests, newImage);

        // then
        assertThat(result).isEqualTo(userId);
        assertThat(existingUser.getNickname()).isEqualTo(userName);
        assertThat(existingUser.getBio()).isEqualTo(bio);
        assertThat(existingUser.getInterests()).isEqualTo(interests);
        assertThat(existingUser.getProfileImagePath()).startsWith("user/" + userId + "/");
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).update(existingUser);
        verify(storageService, times(1)).store(eq(newImage), startsWith("user/" + userId + "/"));

        mockedEvents.verify(() -> Events.publishEvent(any(FileRollbackEvent.class)), times(1));
        mockedEvents.verify(() -> Events.publishEvent(any(FileDeletedEvent.class)), times(1));
    }

    @Test
    void 유저_프로필_업데이트_성공_새_이미지_업로드_기존_이미지_없음() {
        // given
        UUID userId = UUID.randomUUID();
        String userName = "새로운 이름";
        String bio = "새로운 소개";
        Set<InterestType> interests = Set.of(InterestType.PROGRAMMING);
        MultipartFile newImage = mock(MultipartFile.class);

        when(newImage.isEmpty()).thenReturn(false);

        User existingUser = User.builder()
                .id(userId)
                .email("test@gmail.com")
                .nickname("기존 이름")
                .providerType(ProviderType.GOOGLE)
                .role(Role.USER)
                .profileImagePath(null)
                .build();

        when(userRepository.findById(userId)).thenReturn(existingUser);
        when(userRepository.update(any(User.class))).thenReturn(existingUser);

        // when
        UUID result = userCommandService.updateProfile(userId, userName, bio, interests, newImage);

        // then
        assertThat(result).isEqualTo(userId);
        assertThat(existingUser.getNickname()).isEqualTo(userName);
        assertThat(existingUser.getBio()).isEqualTo(bio);
        assertThat(existingUser.getInterests()).isEqualTo(interests);
        assertThat(existingUser.getProfileImagePath()).startsWith("user/" + userId + "/");

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).update(existingUser);
        verify(storageService, times(1)).store(eq(newImage), startsWith("user/" + userId + "/"));

        mockedEvents.verify(() -> Events.publishEvent(any(FileRollbackEvent.class)), times(1));
        mockedEvents.verify(() -> Events.publishEvent(any(FileDeletedEvent.class)), never());
    }

    @Test
    void 유저_프로필_업데이트_성공_새_이미지_업로드_기존_이미지_빈_문자열() {
        // given
        UUID userId = UUID.randomUUID();
        String userName = "새로운 이름";
        String bio = "새로운 소개";
        Set<InterestType> interests = Set.of(InterestType.PROGRAMMING);
        MultipartFile newImage = mock(MultipartFile.class);

        when(newImage.isEmpty()).thenReturn(false);

        User existingUser = User.builder()
                .id(userId)
                .email("test@gmail.com")
                .nickname("기존 이름")
                .providerType(ProviderType.GOOGLE)
                .role(Role.USER)
                .profileImagePath("")
                .build();

        when(userRepository.findById(userId)).thenReturn(existingUser);
        when(userRepository.update(any(User.class))).thenReturn(existingUser);

        // when
        UUID result = userCommandService.updateProfile(userId, userName, bio, interests, newImage);

        // then
        assertThat(result).isEqualTo(userId);
        assertThat(existingUser.getNickname()).isEqualTo(userName);
        assertThat(existingUser.getBio()).isEqualTo(bio);
        assertThat(existingUser.getInterests()).isEqualTo(interests);
        assertThat(existingUser.getProfileImagePath()).startsWith("user/" + userId + "/");

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).update(existingUser);
        verify(storageService, times(1)).store(eq(newImage), startsWith("user/" + userId + "/"));

        mockedEvents.verify(() -> Events.publishEvent(any(FileRollbackEvent.class)), times(1));
        mockedEvents.verify(() -> Events.publishEvent(any(FileDeletedEvent.class)), never());
    }

    @Test
    void 존재하지_않는_유저_프로필_업데이트시_예외_발생() {
        // given
        UUID nonExistId = UUID.randomUUID();
        String userName = "새로운 이름";
        String bio = "새로운 소개";
        Set<InterestType> interests = Set.of(InterestType.PROGRAMMING);

        when(userRepository.findById(nonExistId)).thenThrow(new UserNotFoundException());

        // when & then
        assertThatThrownBy(
                () -> userCommandService.updateProfile(nonExistId, userName, bio, interests, null))
                .isInstanceOf(UserNotFoundException.class);

        verify(userRepository, times(1)).findById(nonExistId);
        verify(userRepository, never()).update(any(User.class));
        verify(storageService, never()).store(any(), any());
        mockedEvents.verify(() -> Events.publishEvent(any()), never());
    }

    @Test
    @DisplayName("스토리지 저장 실패 시 예외 전파")
    void 스토리지_저장_실패시_예외_전파() {
        // given
        UUID userId = UUID.randomUUID();
        String userName = "새로운 이름";
        String bio = "새로운 소개";
        Set<InterestType> interests = Set.of(InterestType.PROGRAMMING);
        MultipartFile newImage = mock(MultipartFile.class);

        when(newImage.isEmpty()).thenReturn(false);

        User existingUser = User.builder()
                .id(userId)
                .email("test@gmail.com")
                .nickname("기존 이름")
                .providerType(ProviderType.GOOGLE)
                .role(Role.USER)
                .build();

        when(userRepository.findById(userId)).thenReturn(existingUser);
        doThrow(new StorageException(StorageErrorCode.FILE_UPLOAD_FAILED))
                .when(storageService).store(any(), any());

        // when & then
        assertThatThrownBy(
                () -> userCommandService.updateProfile(userId, userName, bio, interests, newImage))
                .isInstanceOf(StorageException.class)
                .hasMessage(StorageErrorCode.FILE_UPLOAD_FAILED.getMessage());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).update(any(User.class));
        verify(storageService, times(1)).store(eq(newImage), startsWith("user/" + userId + "/"));
    }

}