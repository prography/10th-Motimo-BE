package kr.co.infra.storage.service.supabase;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "supabase.storage")
@Getter
@RequiredArgsConstructor
public class SupabaseProperties {

    private final String url;
    private final String region;
    private final String bucket;
    private final String apiKey;
    private final String secretKey;
}