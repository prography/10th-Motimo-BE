package kr.co.domain.common.pagination;

import java.util.List;
import java.util.function.Function;

public record CustomSlice<T>(List<T> content, boolean hasNext) {

    public <U> CustomSlice<U> map(Function<T, U> converter) {
        List<U> convertedContent = content.stream()
                .map(converter)
                .toList();
        return new CustomSlice<>(convertedContent, hasNext);
    }
}