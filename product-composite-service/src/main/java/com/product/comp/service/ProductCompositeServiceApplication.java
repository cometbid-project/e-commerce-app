package com.product.comp.service;

import java.util.ArrayList;
import java.util.List;

import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.customizers.OpenApiCustomiser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication(scanBasePackages = {"com.product.service.api","com.product.service.api"})
//@ComponentScan("com.product.servise")
public class ProductCompositeServiceApplication {

    
	public static void main(String[] args) {
		SpringApplication.run(ProductCompositeServiceApplication.class, args);
	}
	
}
