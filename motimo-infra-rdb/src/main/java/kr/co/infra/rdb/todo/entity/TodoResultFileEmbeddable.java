package kr.co.infra.rdb.todo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import kr.co.domain.todo.TodoResultFile;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class TodoResultFileEmbeddable {

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "mime_type")
    private String mimeType;

    public static TodoResultFileEmbeddable from(TodoResultFile file) {
        return new TodoResultFileEmbeddable(file.getFilePath(), file.getFileName(),
                file.getMimeType());
    }

    public TodoResultFile toDomain() {
        return TodoResultFile.of(this.filePath, this.fileName, this.mimeType);
    }
}
