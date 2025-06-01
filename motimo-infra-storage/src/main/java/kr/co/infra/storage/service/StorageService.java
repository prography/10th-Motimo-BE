package kr.co.infra.storage.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String uploadImage(MultipartFile file, String fileName);

    void deleteImage(String filePath);
}
