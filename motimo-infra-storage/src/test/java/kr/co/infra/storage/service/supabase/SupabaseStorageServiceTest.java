package kr.co.infra.storage.service.supabase;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;
import kr.co.infra.storage.exception.StorageErrorCode;
import kr.co.infra.storage.exception.StorageException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

@DisplayName("supabase storage 테스트")
class SupabaseStorageServiceTest {

    private MockWebServer mockWebServer;
    private SupabaseStorageService storageService;
    private String region;
    private String bucket;
    private String apiKey;
    private String secretKey;

    static Stream<Arguments> fileProvider() {
        return Stream.of(
                Arguments.of("텍스트.txt", "text/plain", "hello text".getBytes()),
                Arguments.of("이미지.png", "image/png", new byte[]{(byte) 0x89, 0x50, 0x4E, 0x47}),
                Arguments.of("pdf.pdf", "application/pdf", new byte[]{0x25, 0x50, 0x44, 0x46})
        );
    }

    @BeforeEach
    void setUp() throws Exception {
        region = "test-region";
        bucket = "test-bucket";
        apiKey = "test-api-key";
        secretKey = "test-secret-key";
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        String mockBaseUrl = mockWebServer.url("/").toString().replaceAll("/$", "");

        SupabaseProperties properties = new SupabaseProperties(
                mockBaseUrl, region, bucket, apiKey, secretKey);

        WebClient.Builder webClientBuilder = WebClient.builder();
        storageService = new SupabaseStorageService(webClientBuilder, properties);
    }

    @AfterEach
    void close() throws Exception {
        mockWebServer.shutdown();
    }

    @ParameterizedTest
    @MethodSource("fileProvider")
    void store_성공적으로_업로드되면_URL을_반환한다(String filePath, String contentType, byte[] content)
            throws Exception {
        // given
        MultipartFile file = new MockMultipartFile("file", filePath, contentType, content);
        mockWebServer.enqueue(new MockResponse().setResponseCode(200));
        String encodedFilePath = URLEncoder.encode(filePath, StandardCharsets.UTF_8);

        // when
        storageService.store(file, filePath);

        // then
        RecordedRequest request = mockWebServer.takeRequest();
        assertThat(request.getMethod()).isEqualTo("POST");
        assertThat(request.getPath()).isEqualTo(
                String.format("/storage/v1/object/%s/%s", bucket, encodedFilePath));
        assertThat(request.getHeader("apikey")).isEqualTo(apiKey);
    }

    @Test
    void 파일이_null이면_null을_반환한다() {
        // when & then
        assertThatThrownBy(() -> storageService.store(null, null))
                .isInstanceOf(StorageException.class)
                .hasMessage(StorageErrorCode.INVALID_FILE.getMessage());
    }

    @Test
    void 저장_실패시_예외를_던진다() {
        // given
        MultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "file content".getBytes()
        );
        mockWebServer.enqueue(new MockResponse().setResponseCode(500)); // 서버 오류

        // when & then
        assertThatThrownBy(() -> storageService.store(file, "test.txt"))
                .isInstanceOf(StorageException.class)
                .hasMessageContaining(StorageErrorCode.FILE_UPLOAD_FAILED.getMessage());
    }

    @Test
    void 성공적으로_삭제() throws Exception {
        // given
        mockWebServer.enqueue(new MockResponse().setResponseCode(200));
        String filePath = "test.txt"; // 상대 경로로 수정

        // when & then
        assertThatCode(() -> storageService.delete(filePath)).doesNotThrowAnyException();

        RecordedRequest request = mockWebServer.takeRequest();
        assertThat(request.getMethod()).isEqualTo("DELETE");
        assertThat(request.getPath()).isEqualTo("/storage/v1/object/test-bucket/test.txt");
    }

    @Test
    void 삭제_실패시_예외를_던진다() {
        // given
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));
        String fileUrl = mockWebServer.url("/storage/v1/object/test-bucket/test.txt").toString();

        // when & then
        assertThatThrownBy(() -> storageService.delete(fileUrl))
                .isInstanceOf(StorageException.class)
                .hasMessageContaining(StorageErrorCode.FILE_DELETE_FAILED.getMessage());
    }
}