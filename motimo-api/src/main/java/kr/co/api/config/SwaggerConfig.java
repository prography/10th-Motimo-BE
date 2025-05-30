package kr.co.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
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
                .addOpenApiCustomizer(openApi -> {
                    jwtSecuritySetting(openApi);
                    openApi.setInfo(
                            new Info()
                                    .title("Motimo API List")
                                    .description("모티모 API 목록입니다.")
                                    .version("1.0.0")
                    );
                })
                .build();
    }


    @Bean
    public GroupedOpenApi version1() {
        return GroupedOpenApi
                .builder()
                .group("Motimo V1 API List")
                .pathsToMatch("/v1/**")
                .addOpenApiCustomizer(openApi -> {
                    jwtSecuritySetting(openApi);
                    openApi.setInfo(
                            new Info()
                                    .title("Motimo Version1 API List")
                                    .description("모티모 버전 1 API 목록입니다.")
                                    .version("1.0.0")
                    );
                })
                .build();
    }


    private void jwtSecuritySetting(OpenAPI openApi) {
        openApi.getComponents().addSecuritySchemes("BearerAuth",
                new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .in(SecurityScheme.In.HEADER)
        );

        openApi.addSecurityItem(new SecurityRequirement().addList("BearerAuth"));
    }

}