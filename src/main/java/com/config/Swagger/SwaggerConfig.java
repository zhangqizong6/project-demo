package com.config.Swagger;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

/**
 * @ClassName: SwaggerConfig
 * @author: zqz
 * @date: 2024/3/21 22:10
 */
@Configuration
@EnableSwagger2
@ConditionalOnExpression("${swagger.enable:true}")
public class SwaggerConfig {


    @Value("${doc.title}")
    private String title;

    @Value("${doc.description}")
    private String description;

    @Value("${doc.version}")
    private String version;


    @Value("${knife4j.production}")
    Boolean production;

    @Bean
    public Docket customImplementation() {
        ApiInfo apiInfo = new ApiInfoBuilder()
                .title(title)
                .description(description)
                .version(version)
                .build();

        return new Docket(SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.withMethodAnnotation(ApiOperation.class))
                .build()
                .enable(!production)
                .apiInfo(apiInfo)
                .useDefaultResponseMessages(false);
    }

}
