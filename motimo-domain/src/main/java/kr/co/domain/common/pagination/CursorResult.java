package kr.co.domain.common.pagination;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public record CursorResult<T>(
        List<T> content,
        boolean hasBefore,
        boolean hasAfter
) {

    public <U> CursorResult<U> map(Function<T, U> converter) {
        List<U> convertedContent = content.stream()
                .map(converter)
                .toList();
        return new CursorResult<>(convertedContent, hasBefore, hasAfter);
    }

    public <U> CursorResult<U> mapNotNull(Function<T, U> fn) {
        List<U> list = content.stream().map(fn)
                .filter(Objects::nonNull)
                .toList();
        return new CursorResult<>(list, hasBefore, hasAfter);
    }
}