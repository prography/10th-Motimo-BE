package kr.co.infra.storage.config;

import kr.co.infra.storage.service.supabase.SupabaseProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SupabaseProperties.class)
public class SupabaseStorageConfig {

}
