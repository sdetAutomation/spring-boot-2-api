package com.sdet.auto.springboot2api.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket userApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Users")
                .apiInfo(getUserApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sdet.auto.springboot2api"))
                .paths(PathSelectors.ant("/users/**"))
                .build();
    }

    @Bean
    public Docket orderApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("Orders")
                .apiInfo(getOrderApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sdet.auto.springboot2api"))
                .paths(PathSelectors.ant("/orders/**"))
                .build();
    }

    // swagger metadata: http://localhost:8080/v2/api-docs
    // swagger ui url: http://localhost:8080/swagger-ui.html

    private ApiInfo getUserApiInfo() {
        return new ApiInfoBuilder()
                .title("sdetAutomation's User Api")
                .description("This page lists all User Api's")
                .version("2.0")
                .contact(new Contact("sdetAutomation", "https://github.com/sdetAutomation/", "sdet.testautomation@gmail.com"))
                .license("MIT License")
                .licenseUrl("https://github.com/sdetAutomation/spring-boot-2-api/blob/master/LICENSE")
                .build();
    }

    private ApiInfo getOrderApiInfo() {
        return new ApiInfoBuilder()
                .title("sdetAutomation's Order Api")
                .description("This page lists all Order Api's")
                .version("2.0")
                .contact(new Contact("sdetAutomation", "https://github.com/sdetAutomation/", "sdet.testautomation@gmail.com"))
                .license("MIT License")
                .licenseUrl("https://github.com/sdetAutomation/spring-boot-2-api/blob/master/LICENSE")
                .build();
    }
}
