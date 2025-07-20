package kr.co.domain.common.pagination;

import java.util.List;
import java.util.function.Function;

public record CustomPage<T>(List<T> content, long totalCount, int totalPage, int page, int size) {

    public <U> CustomPage<U> map(Function<T, U> converter) {
        List<U> convertedContent = content.stream()
                .map(converter)
                .toList();
        return new CustomPage<>(convertedContent, totalCount, totalPage, page, size);
    }
}