package com.product.comp.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Product Service Documentation")
                .description("Product Composite service -Reactive API Documentation")
                .version("1.0.0")
                .build();
    }
    
	@Bean
	public Docket docket() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(this.apiInfo())
                .enable(true)
				.select()
					.apis(RequestHandlerSelectors.basePackage("com.product.service.api.composite.prod"))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(new ApiInfoBuilder().version("1.0").title("Product Composite API")
						.description("Documentation Product Composite API v1.0").build());
	}
}