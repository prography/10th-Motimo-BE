package kr.co.infra.storage.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    String store(MultipartFile file, String fileName);

    //    Path load(String filename);
    void delete(String filePath);
}
