package com.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@ComponentScan({"com.product.service.api", "com.product.service.util"})
@EnableSwagger2
@OpenAPIDefinition(info =
   @Info(title = "Product API", version = "1.0", description = "Documentation Product API v1.0")
)
public class ProductServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductServiceApplication.class, args);
	}
	
	 private ApiInfo apiInfo() {
	        return new ApiInfoBuilder()
	                .title("Reactive Documentation")
	                .description("Reactive API Documentation")
	                .version("1.0.0")
	                .build();
	}
	    
	
	@Bean
	public Docket swaggerPersonApi10() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
					.apis(RequestHandlerSelectors.basePackage("com.product.service.api.core.product"))
					.paths(PathSelectors.any())
				.build()
				.apiInfo(new ApiInfoBuilder().version("1.0")
						.title("Product API")
						.description("Documentation Product API v1.0")
						.build());
	}

}
