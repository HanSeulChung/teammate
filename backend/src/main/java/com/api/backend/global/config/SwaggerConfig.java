package com.api.backend.global.config;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

  private static final String REFERENCE = "Authorization 헤더 값";

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo())
        .useDefaultResponseMessages(false)
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.api.backend"))
        .paths(PathSelectors.any())
        .build()
        .securityContexts(List.of(securityContext()))
        .securitySchemes(List.of(securityScheme()));
  }
  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("TeamMate 프로젝트")
        .description("협업 관련 서비스를 제공하는 백엔드 API 입니다.")
        .version("2.0")
        .build();
  }

  private SecurityContext securityContext() {
    return springfox.documentation.spi
        .service.contexts.SecurityContext
        .builder()
        .securityReferences(defaultAuth())
        .operationSelector(operationContext -> true)
        .build();
  }
  private List<SecurityReference> defaultAuth() {
    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    authorizationScopes[0] = new AuthorizationScope("global", "accessEverything");
    return List.of(new SecurityReference(REFERENCE, authorizationScopes));
  }

  private ApiKey securityScheme() {
    return new ApiKey(REFERENCE, "Authorization", "header");
  }
}