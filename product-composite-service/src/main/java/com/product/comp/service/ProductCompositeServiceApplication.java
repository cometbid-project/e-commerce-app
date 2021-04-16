package com.product.comp.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication(scanBasePackages = {"com.product.service.api","com.product.service.api"})
//@ComponentScan("com.product.servise")
public class ProductCompositeServiceApplication {

    
	public static void main(String[] args) {
		SpringApplication.run(ProductCompositeServiceApplication.class, args);
	}

       
}
