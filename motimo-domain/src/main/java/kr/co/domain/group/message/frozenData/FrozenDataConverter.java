package kr.co.domain.group.message.frozenData;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class FrozenDataConverter {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T convertTo(Map<String, String> frozenData, Class<T> targetClass) {
        if (frozenData == null) return null;
        return objectMapper.convertValue(frozenData, targetClass);
    }

    public static Map<String, String> convertFrom(Object data) {
        if (data == null) return null;
        return objectMapper.convertValue(data, new TypeReference<>() {
        });
    }
}
