package kr.co.infra.storage.service.supabase;

import kr.co.infra.storage.exception.StorageErrorCode;
import kr.co.infra.storage.exception.StorageException;
import kr.co.infra.storage.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupabaseStorageService implements StorageService {

    private final WebClient.Builder webClientBuilder;
    private final SupabaseProperties properties;

    @Override
    public void store(MultipartFile file, String filePath) {

        if (file == null || file.isEmpty()) {
            throw new StorageException(StorageErrorCode.INVALID_FILE);
        }

        WebClient webClient = getWebClient();
        byte[] fileBytes = getBytes(file);
        String contentType = file.getContentType() != null ? file.getContentType()
                : MediaType.APPLICATION_OCTET_STREAM_VALUE;

        try {
            webClient.post()
                    .uri("/storage/v1/object/{bucket}/{filePath}", properties.getBucket(), filePath)
                    .contentType(MediaType.parseMediaType(contentType))
                    .bodyValue(fileBytes)
                    .retrieve()
                    .toBodilessEntity()
                    .block();

        } catch (Exception e) {
            log.error("파일 업로드 중 예외 발생", e);
            throw new StorageException(StorageErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    @Override
    public String getFileUrl(String filePath) {
        return generateFileUrl(filePath);
    }

    @Override
    public void delete(String filePath) {
        WebClient webClient = getWebClient();
        String fileUrl = generateFileUrl(filePath);
        try {
            webClient.delete()
                    .uri(fileUrl)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        } catch (Exception e) {
            log.error("파일 삭제 중 예외 발생", e);
            throw new StorageException(StorageErrorCode.FILE_DELETE_FAILED);
        }
    }

    private byte[] getBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (Exception e) {
            throw new RuntimeException("파일 변환 실패", e);
        }
    }

    private WebClient getWebClient() {
        return webClientBuilder
                .baseUrl(properties.getUrl())
                .defaultHeader("apikey", properties.getApiKey())
                .defaultHeader("Authorization", "Bearer " + properties.getApiKey())
                .build();
    }

    private String generateFileUrl(String filePath) {
        return String.format("%s/storage/v1/object/%s/%s", properties.getUrl(),
                properties.getBucket(), filePath);
    }
}
