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
    public String uploadImage(MultipartFile image, String fileName) {

        if (image == null || image.isEmpty()) {
            return null;
        }

        WebClient webClient = getWebClient();

        byte[] fileBytes = getBytes(image);
        String contentType = image.getContentType() != null ? image.getContentType()
                : MediaType.APPLICATION_OCTET_STREAM_VALUE;

        try {
            webClient.post()
                    .uri("/storage/v1/object/{bucket}/{filename}", properties.getBucket(), fileName)
                    .contentType(MediaType.parseMediaType(contentType))
                    .bodyValue(fileBytes)
                    .retrieve()
                    .toBodilessEntity()
                    .block();
            return fileName;

        } catch (Exception e) {
            log.error("파일 업로드 중 예외 발생", e);
            throw new StorageException(StorageErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    @Override
    public void deleteImage(String fileName) {
        WebClient webClient = getWebClient();

        try {
            webClient.delete()
                    .uri("/storage/v1/object/{bucket}/{fileName}", properties.getBucket(), fileName)
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
}
