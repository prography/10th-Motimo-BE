package kr.co.api.config;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi motimoAPI() {
        return GroupedOpenApi
                .builder()
                .group("Motimo API List")
                .addOpenApiCustomizer(
                        openApi ->
                                openApi
                                        .setInfo(
                                                new Info()
                                                        .title("Motimo API List") // API 제목
                                                        .description("모티모 API 목록입니다.") // API 설명
                                                        .version("1.0.0") // API 버전
                                        )
                )
                .build();
    }

    @Bean
    public GroupedOpenApi version1() {
        return GroupedOpenApi
                .builder()
                .group("Motimo V1 API List")
                .pathsToMatch("/v1/**")
                .addOpenApiCustomizer(
                        openApi ->
                                openApi
                                        .setInfo(
                                                new Info()
                                                        .title("Motimo Version1 API List")
                                                        .description("모티모 버전 1 API 목록입니다.")
                                                        .version("1.0.0")
                                        )
                )
                .build();
    }


}