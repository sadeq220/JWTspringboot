package com.sadeqstore.demo.swaggerConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import springfox.documentation.service.*;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@EnableSwagger2
public class SwConfig {

    @Bean
    public Docket swaggerCustomization(){
        return new Docket(DocumentationType.SWAGGER_2).groupName("REST endpoints")
                .ignoredParameterTypes(Authentication.class).select()
                //.paths(PathSelectors.ant("/actuator/**"))
                .apis(RequestHandlerSelectors.basePackage("com.sadeqstore.demo.controller"))
                .build().apiInfo(metadata())
                .useDefaultResponseMessages(false)//
                .securitySchemes(Collections.singletonList(apiKey()))
                .securityContexts(Collections.singletonList(securityContext()))
                .tags(new Tag("users", "Operations about users"))//
                .genericModelSubstitutes(Optional.class);
    }
    private ApiInfo metadata() {
        return new ApiInfoBuilder()
                .title("sadeq store protected with JWT")
                .description("This is a sample store protected with JWT")
                .version("1.0.0")
                //.license("MIT License").licenseUrl("http://opensource.org/licenses/MIT")
                .contact(new Contact("sadeq", null, "tsts220g@gmail.com"))
                .build();
    }
    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.any())
                .build();
    }
    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = {authorizationScope};
        return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
    }

}
