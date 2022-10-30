package com.mapnote.mapnoteserver.domain.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  @Bean
  public GroupedOpenApi userApi() {
    return GroupedOpenApi.builder()
        .group("유저 관련 API")
        .pathsToMatch("/api/v1/users/**")
        .build();
  }

  @Bean
  public GroupedOpenApi mailApi() {
    return GroupedOpenApi.builder()
        .group("메일 관련 API")
        .pathsToMatch("/api/v1/mail/**")
        .build();
  }

  @Bean
  public GroupedOpenApi mapApi() {
    return GroupedOpenApi.builder()
        .group("장소 검색 관련 API")
        .pathsToMatch("/api/v1/map/**")
        .build();
  }

  @Bean
  public OpenAPI springShopOpenAPI() {
    return new OpenAPI()
        .info(new Info().title("Mapnote SWAGGER")
            .description("Mapnote Swagger - API 문서"));
  }
}