package lgcns.shuttle.api.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.models.GroupedOpenApi;

import java.util.Arrays;

@OpenAPIDefinition(
        info = @Info(title = "RCS WAS",
                description = "RCS WAS Solution",
                version = "v0.1"))
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI(){
        SecurityScheme accessSecurityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name("Authorization");

        SecurityScheme refreshSecurityScheme = new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("Refreshtoken");

        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList("bearerAuth")
                .addList("refreshToken");

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", accessSecurityScheme)
                        .addSecuritySchemes("refreshToken", refreshSecurityScheme)
                )
                .addSecurityItem(securityRequirement);
    }

    @Bean
    public GroupedOpenApi allOpenApi() {
        String[] paths = {"/**"};

        return GroupedOpenApi.builder()
                .group("ALL")
                .pathsToMatch(paths)
                .build();
    }

    @Bean
    public GroupedOpenApi aOpenApi() {
        String[] paths = {"/test/**"};

        return GroupedOpenApi.builder()
                .group("V2")
                .pathsToMatch(paths)
                .build();
    }
}
