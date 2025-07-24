package kr.co.domain.todo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TodoResultFile {

    private String filePath;
    private String fileName;
    private String mimeType;

    public static TodoResultFile of(String filePath, String fileName, String mimeType) {
        return new TodoResultFile(filePath, fileName, mimeType);
    }
}
