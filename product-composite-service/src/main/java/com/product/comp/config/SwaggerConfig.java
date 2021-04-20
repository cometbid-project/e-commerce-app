package com.product.comp.config;

import static java.util.Collections.emptyList;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
	
	    @Value("${api.common.version}")           String apiVersion;
	    @Value("${api.common.title}")             String apiTitle;
	    @Value("${api.common.description}")       String apiDescription;
	    @Value("${api.common.termsOfServiceUrl}") String apiTermsOfServiceUrl;
	    @Value("${api.common.license}")           String apiLicense;
	    @Value("${api.common.licenseUrl}")        String apiLicenseUrl;
	    @Value("${api.common.contact.name}")      String apiContactName;
	    @Value("${api.common.contact.url}")       String apiContactUrl;
	    @Value("${api.common.contact.email}")     String apiContactEmail;

		/**
		 * Will exposed on $HOST:$PORT/swagger-ui/index.html
		 *
		 * @return
		 */
		@Bean
		public Docket apiDocumentation() {

			return new Docket(SWAGGER_2)
				.select()
				.apis(basePackage("com.product.comp.service"))
				.paths(PathSelectors.any())
				.build()
					.globalResponses(POST, emptyList())
					.globalResponses(GET, emptyList())
					.globalResponses(DELETE, emptyList())
					.apiInfo(new ApiInfo(
	                    apiTitle,
	                    apiDescription,
	                    apiVersion,
	                    apiTermsOfServiceUrl,
	                    new Contact(apiContactName, apiContactUrl, apiContactEmail),
	                    apiLicense,
	                    apiLicenseUrl,
	                    emptyList()
	                ));
	    }

}