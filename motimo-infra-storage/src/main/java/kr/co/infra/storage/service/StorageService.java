package kr.co.infra.storage.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    void store(MultipartFile file, String filePath);

    String getFileUrl(String filePath);

    void delete(String filePath);
}
